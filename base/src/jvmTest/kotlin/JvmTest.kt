import com.example.MyJvmClass
import kotlin.test.Test
import kotlin.test.assertEquals
import my.generated.*

class JvmTest {
    @Test
    fun testMyQualifiedName() {
        assertEquals("com.example.MyJvmClass", MyJvmClass().qualifiedClassName)
    }

    @Test
    fun testQualifiedName() {
        assertEquals("com.example.MyJvmClass", MyJvmClass().javaClass.name)
    }
}
