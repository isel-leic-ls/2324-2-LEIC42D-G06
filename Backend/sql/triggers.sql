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