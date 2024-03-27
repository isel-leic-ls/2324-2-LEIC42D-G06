create or replace function check_session_capacity()
returns trigger
language plpgsql
as $$

declare
    current_capacity INT;
    max_capacity INT;

begin

    select count(sp.*), max(s.capacity)
    into current_capacity, max_capacity
    from Session s
    left join SessionPlayer sp on s.sid = sp.session_id
    where s.sid = NEW.session_id;

    if exists(select 1 from Session where sid = NEW.session_id and closed = true) then
        raise exception 'Session is already closed';
    end if;

    current_capacity := current_capacity + 1;
    if current_capacity = max_capacity then
        update Session set closed = true where sid = NEW.session_id;
    end if;

    return NEW;
end;
$$;


create trigger before_insert_session_player
before insert on SessionPlayer
for each row
execute function check_session_capacity();


create or replace function check_player_count()
returns trigger
language plpgsql
as $$

declare
    current_count INT;
    closed_flag BOOLEAN;
begin
    select count(sp.*)
    into current_count
    from SessionPlayer sp
    where sp.session_id = OLD.session_id;

    select closed into closed_flag from Session where sid = OLD.session_id;

    if current_count = 0 then
        delete from Session where sid = OLD.session_id;
    elsif closed_flag = true then
        update Session set closed = false where sid = OLD.session_id;
    end if;

    return null;
end;
$$;

create trigger after_delete_session_player
after delete on SessionPlayer
for each row
execute function check_player_count();


create or replace function check_session_capacity_update()
returns trigger
language plpgsql
as $$

declare
    current_players_count INT;
begin
    select count(sp.*)
    into current_players_count
    from SessionPlayer sp
    where sp.session_id = NEW.sid;

    if NEW.capacity > OLD.capacity And OLD.closed = true then
        update Session set closed = false where sid = NEW.sid;
    elsif NEW.capacity < OLD.capacity And current_players_count = NEW.capacity then
        update Session set closed = true where sid = NEW.sid;
    end if;

    return NEW;
end;
$$;


create trigger after_update_session
after update on Session
for each row
execute function check_session_capacity_update();
