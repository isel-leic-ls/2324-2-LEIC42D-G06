package pt.isel.ls

import org.junit.Test
import pt.isel.ls.utils.dataSource
import kotlin.test.assertEquals


data class Student(val id : Int, val name : String, val email : String)

class Phase0Tests {
    @Test
    fun `student retrieval test`() {
        //arrange
        val student = Student(47718, "Pedro Diz", "47718@alunos.isel.pt")
        var retrievedStudent : Student? = null

        //act
        dataSource.connection.use {
            val stm = it.prepareStatement("select * from students where id = 47718")
            val rs = stm.executeQuery()
            if(rs.next()) {
                retrievedStudent = Student(rs.getInt("id"), rs.getString("name"), rs.getString("email"))
            }
        }
        //assert
        assertEquals(student, retrievedStudent)
    }
}