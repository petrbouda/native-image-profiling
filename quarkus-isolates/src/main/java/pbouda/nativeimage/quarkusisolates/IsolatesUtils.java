package pbouda.nativeimage.quarkusisolates;

import org.graalvm.nativeimage.IsolateThread;
import org.graalvm.nativeimage.ObjectHandle;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public class IsolatesUtils {

    /**
     * Copies a {@link String} from a source isolate to a target isolate. This
     * method is the part that is executed in the source isolate.
     *
     * An isolate cannot directly access Java objects from another isolate.
     * Therefore, we convert the source Java string to a C string, and pass the C
     * string to the target isolate. The target isolate then converts the C string
     * back to the target Java string.
     *
     * We use the utility functions in {@link CTypeConversion} for the C string
     * conversions. Note that there are other more efficient ways to copy the
     * string, but this approach is the easiest.
     *
     * The return value is a handle to the string in the target isolate. Only the
     * target isolate can access and resolve that handle, the source isolate must
     * treat the handle as an opaque value.
     */
    private static ObjectHandle copyString(IsolateThread targetContext, String sourceString) {
        /* Convert the source Java string to a C string. */
        try (CTypeConversion.CCharPointerHolder cStringHolder = CTypeConversion.toCString(sourceString)) {
            /* Cross the isolate boundary with a C string as the parameter. */
            return copyString(targetContext, cStringHolder.get());
        }
    }

    /**
     * Copies a {@link String} from a source isolate to a target isolate. This
     * method is the part that is executed in the target isolate.
     */
    @CEntryPoint
    private static ObjectHandle copyString(@CEntryPoint.IsolateThreadContext IsolateThread renderingContext, CCharPointer cString) {
        /* Convert the C string to the target Java string. */
        String targetString = CTypeConversion.toJavaString(cString);
        /* Encapsulate the target string in a handle that can be returned back to the source isolate. */
        return ObjectHandles.getGlobal().create(targetString);
    }

}
