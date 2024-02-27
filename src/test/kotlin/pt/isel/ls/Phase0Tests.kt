package pt.isel.ls

import org.junit.Test
import pt.isel.ls.utils.dataSource
import kotlin.test.assertEquals


data class Student(val id: Int, val name: String, val course: Int)

class Phase0Tests {
    @Test
    fun `student retrieval test`() {
        //arrange
        val student = Student(47718, "Pedro Diz", 1)
        var retrievedStudent: Student? = null

        //act
        dataSource.connection.use {
            val stm = it.prepareStatement("select * from students where number = 47718")
            val rs = stm.executeQuery()
            if (rs.next()) {
                retrievedStudent = Student(rs.getInt("id"), rs.getString("name"), rs.getInt("course"))
            }
        }
        //assert
        assertEquals(student, retrievedStudent)
    }

    @Test
    fun `student insertion test`() {
        //act
        val sut = Student(48259, "Vasco Branco", 1)
        dataSource.connection.use {
            val stm = it.prepareStatement("insert into students values(${sut.id},'${sut.name}',${sut.course})")
            stm.executeUpdate() // Use executeUpdate() for INSERT, UPDATE, DELETE queries
            val stm2 = it.prepareStatement("select * from students where number = 48259")
            val rs = stm2.executeQuery()
            if (rs.next()) {
                assertEquals(sut.id, rs.getInt("number"))
                assertEquals(sut.name, rs.getString("name"))
                assertEquals(sut.course, rs.getInt("course"))
            } else {
                throw Exception("Student wasn't inserted!")
            }
        }
    }


}