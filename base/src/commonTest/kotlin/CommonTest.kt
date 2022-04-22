import com.example.MyCommonClass
import kotlin.test.Test
import kotlin.test.assertEquals
import my.generated.*

class CommonTest {
    @Test
    fun testMyQualifiedName() {
        assertEquals("com.example.MyCommonClass", MyCommonClass().qualifiedClassName)
    }
}
