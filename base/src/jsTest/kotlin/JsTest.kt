import com.example.MyJsClass
import kotlin.test.Test
import kotlin.test.assertEquals
import my.generated.*

class JsTest {
    @Test
    fun testMyQualifiedName() {
        assertEquals("com.example.MyJsClass", MyJsClass().qualifiedClassName)
    }

    @Test
    fun testSimpleName() {
        assertEquals("MyJsClass", MyJsClass::class.simpleName)  // no qualified name available in standard Kotlin/Js
    }
}
