package pt.isel.ls

import org.postgresql.ds.PGSimpleDataSource
import pt.isel.ls.domain.Student
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class Phase0Tests {
    private val dataSource = PGSimpleDataSource().apply {
        setUrl(System.getenv("JDBC_DATABASE_URL"))
    }

    private val studentToRetrieve = Student(47718, "Pedro Diz", 1)
    private val studentToUpdate = Student(48259, "Vasco Branco", 1)
    private val studentToDelete = Student(48264, "João Pereira", 1)

    @BeforeTest
    fun setup() {
        dataSource.connection.use {
            //Add student to the database for testing how the test behaves when the student is found
            val stm0 =
                it.prepareStatement("select * from students where number = ${studentToRetrieve.number}")
            val rs0 = stm0.executeQuery()
            if (!rs0.next()) { //if student does not exist
                val insertStm = it.prepareStatement("insert into students values (?, ?, ?)")
                insertStm.setInt(1, studentToRetrieve.number)
                insertStm.setString(2, studentToRetrieve.name)
                insertStm.setInt(3, studentToRetrieve.course)
                insertStm.executeUpdate()
            }

            //Delete student to update if it exists in the database for testing his insertion and update
            val stm1 =
                it.prepareStatement("select * from students where number = ${studentToUpdate.number}")
            val rs1 = stm1.executeQuery()
            if (rs1.next()) { //if student exists
                val deleteStm =
                    it.prepareStatement("delete from students where number = ${studentToUpdate.number}")
                deleteStm.executeUpdate()
            }

            //Add student to the database for testing how the test behaves when the student is deleted
            val stm2 =
                it.prepareStatement("select * from students where number = ${studentToDelete.number}")
            val rs2 = stm2.executeQuery()
            if (!rs2.next()) { //if student does not exist
                val insertStm = it.prepareStatement("insert into students values (?, ?, ?)")
                insertStm.setInt(1, studentToDelete.number)
                insertStm.setString(2, studentToDelete.name)
                insertStm.setInt(3, studentToDelete.course)
                insertStm.executeUpdate()
            }
        }
    }

    @Test
    fun `student retrieval test`() {
        //arrange
        val sut = studentToRetrieve
        //act
        dataSource.connection.use {
            //The student is already in the database because of the setup
            val stm = it.prepareStatement("select * from students where number = ${sut.number}")
            val rs = stm.executeQuery()
            if (rs.next()) {
                val retrievedStudent = Student(
                    rs.getInt("number"),
                    rs.getString("name"),
                    rs.getInt("course")
                )
                assertEquals(sut, retrievedStudent)
            } else throw Exception("Student not found")
            //if (rs.next()) throw Exception("More than one student with the same number") //SHOULD WE DELETE THIS LINE?
        }
    }

    @Test
    fun `insert and update a student test`() {
        //arrange
        val sut = studentToUpdate
        val updatedSut = Student(sut.number, "Vasco Branco", sut.course)
        //act
        dataSource.connection.use {
            //The student isn't in the database because of the setup, so we need to insert it first and then update it
            val stm0 = it.prepareStatement("insert into students values (?, ?, ?)")
            stm0.setInt(1, sut.number)
            stm0.setString(2, sut.name)
            stm0.setInt(3, sut.course)
            val inserted = stm0.executeUpdate()
            assertEquals(1, inserted) //check if the student was inserted

            val stm1 = it.prepareStatement("select * from students where number = ${sut.number}")
            val rsBeforeUpdate = stm1.executeQuery()
            if (rsBeforeUpdate.next()) {
                val retrievedStudent = Student(
                    rsBeforeUpdate.getInt("number"),
                    rsBeforeUpdate.getString("name"),
                    rsBeforeUpdate.getInt("course")
                )
                assertEquals(sut, retrievedStudent) //check if the student was inserted correctly
            } else throw Exception("Student inserted not found")

            val stm2 = it.prepareStatement("update students set name = ? where number = ${sut.number}")
            stm2.setString(1, updatedSut.name)
            val updated = stm2.executeUpdate()
            assertEquals(1, updated) //check if the student was updated

            val stm3 = it.prepareStatement("select * from students where number = ${sut.number}")
            val rsAfterUpdate = stm3.executeQuery()
            if (rsAfterUpdate.next()) {
                val retrievedStudent = Student(
                    rsAfterUpdate.getInt("number"),
                    rsAfterUpdate.getString("name"),
                    rsAfterUpdate.getInt("course")
                )
                assertEquals(updatedSut, retrievedStudent) //check if the student was updated correctly
            } else throw Exception("Student updated not found")
        }
    }

    @Test
    fun `delete a student test`() {
        //arrange
        val sut = studentToDelete
        //act
        dataSource.connection.use {
            //The student is already in the database because of the setup
            val stm0 = it.prepareStatement("delete from students where number = ${sut.number}")
            val deleted = stm0.executeUpdate()
            assertEquals(1, deleted)

            val stm1 = it.prepareStatement("select * from students where number = ${sut.number}")
            val rs = stm1.executeQuery()
            if (rs.next()) throw Exception("Student not deleted")
        }
    }
}