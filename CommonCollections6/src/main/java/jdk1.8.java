//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package sun.reflect.annotation;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.io.ObjectInputStream.GetField;
import java.lang.annotation.Annotation;
import java.lang.annotation.AnnotationFormatError;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import sun.misc.Unsafe;

class AnnotationInvocationHandler implements InvocationHandler, Serializable {
    private static final long serialVersionUID = 6182022883658399397L;
    private final Class<? extends Annotation> type;
    private final Map<String, Object> memberValues;
    private transient volatile Method[] memberMethods = null;

    AnnotationInvocationHandler(Class<? extends Annotation> var1, Map<String, Object> var2) {
        Class[] var3 = var1.getInterfaces();
        if (var1.isAnnotation() && var3.length == 1 && var3[0] == Annotation.class) {
            this.type = var1;
            this.memberValues = var2;
        } else {
            throw new AnnotationFormatError("Attempt to create proxy for a non-annotation type.");
        }
    }

    public Object invoke(Object var1, Method var2, Object[] var3) {
        String var4 = var2.getName();
        Class[] var5 = var2.getParameterTypes();
        if (var4.equals("equals") && var5.length == 1 && var5[0] == Object.class) {
            return this.equalsImpl(var3[0]);
        } else if (var5.length != 0) {
            throw new AssertionError("Too many parameters for an annotation method");
        } else {
            byte var7 = -1;
            switch(var4.hashCode()) {
                case -1776922004:
                    if (var4.equals("toString")) {
                        var7 = 0;
                    }
                    break;
                case 147696667:
                    if (var4.equals("hashCode")) {
                        var7 = 1;
                    }
                    break;
                case 1444986633:
                    if (var4.equals("annotationType")) {
                        var7 = 2;
                    }
            }

            switch(var7) {
                case 0:
                    return this.toStringImpl();
                case 1:
                    return this.hashCodeImpl();
                case 2:
                    return this.type;
                default:
                    Object var6 = this.memberValues.get(var4);
                    if (var6 == null) {
                        throw new IncompleteAnnotationException(this.type, var4);
                    } else if (var6 instanceof ExceptionProxy) {
                        throw ((ExceptionProxy)var6).generateException();
                    } else {
                        if (var6.getClass().isArray() && Array.getLength(var6) != 0) {
                            var6 = this.cloneArray(var6);
                        }

                        return var6;
                    }
            }
        }
    }

    private Object cloneArray(Object var1) {
        Class var2 = var1.getClass();
        if (var2 == byte[].class) {
            byte[] var6 = (byte[])((byte[])var1);
            return var6.clone();
        } else if (var2 == char[].class) {
            char[] var5 = (char[])((char[])var1);
            return var5.clone();
        } else if (var2 == double[].class) {
            double[] var4 = (double[])((double[])var1);
            return var4.clone();
        } else if (var2 == float[].class) {
            float[] var11 = (float[])((float[])var1);
            return var11.clone();
        } else if (var2 == int[].class) {
            int[] var10 = (int[])((int[])var1);
            return var10.clone();
        } else if (var2 == long[].class) {
            long[] var9 = (long[])((long[])var1);
            return var9.clone();
        } else if (var2 == short[].class) {
            short[] var8 = (short[])((short[])var1);
            return var8.clone();
        } else if (var2 == boolean[].class) {
            boolean[] var7 = (boolean[])((boolean[])var1);
            return var7.clone();
        } else {
            Object[] var3 = (Object[])((Object[])var1);
            return var3.clone();
        }
    }

    private String toStringImpl() {
        StringBuilder var1 = new StringBuilder(128);
        var1.append('@');
        var1.append(this.type.getName());
        var1.append('(');
        boolean var2 = true;
        Iterator var3 = this.memberValues.entrySet().iterator();

        while(var3.hasNext()) {
            Entry var4 = (Entry)var3.next();
            if (var2) {
                var2 = false;
            } else {
                var1.append(", ");
            }

            var1.append((String)var4.getKey());
            var1.append('=');
            var1.append(memberValueToString(var4.getValue()));
        }

        var1.append(')');
        return var1.toString();
    }

    private static String memberValueToString(Object var0) {
        Class var1 = var0.getClass();
        if (!var1.isArray()) {
            return var0.toString();
        } else if (var1 == byte[].class) {
            return Arrays.toString((byte[])((byte[])var0));
        } else if (var1 == char[].class) {
            return Arrays.toString((char[])((char[])var0));
        } else if (var1 == double[].class) {
            return Arrays.toString((double[])((double[])var0));
        } else if (var1 == float[].class) {
            return Arrays.toString((float[])((float[])var0));
        } else if (var1 == int[].class) {
            return Arrays.toString((int[])((int[])var0));
        } else if (var1 == long[].class) {
            return Arrays.toString((long[])((long[])var0));
        } else if (var1 == short[].class) {
            return Arrays.toString((short[])((short[])var0));
        } else {
            return var1 == boolean[].class ? Arrays.toString((boolean[])((boolean[])var0)) : Arrays.toString((Object[])((Object[])var0));
        }
    }

    private Boolean equalsImpl(Object var1) {
        if (var1 == this) {
            return true;
        } else if (!this.type.isInstance(var1)) {
            return false;
        } else {
            Method[] var2 = this.getMemberMethods();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                Method var5 = var2[var4];
                String var6 = var5.getName();
                Object var7 = this.memberValues.get(var6);
                Object var8 = null;
                sun.reflect.annotation.AnnotationInvocationHandler var9 = this.asOneOfUs(var1);
                if (var9 != null) {
                    var8 = var9.memberValues.get(var6);
                } else {
                    try {
                        var8 = var5.invoke(var1);
                    } catch (InvocationTargetException var11) {
                        return false;
                    } catch (IllegalAccessException var12) {
                        throw new AssertionError(var12);
                    }
                }

                if (!memberValueEquals(var7, var8)) {
                    return false;
                }
            }

            return true;
        }
    }

    private sun.reflect.annotation.AnnotationInvocationHandler asOneOfUs(Object var1) {
        if (Proxy.isProxyClass(var1.getClass())) {
            InvocationHandler var2 = Proxy.getInvocationHandler(var1);
            if (var2 instanceof sun.reflect.annotation.AnnotationInvocationHandler) {
                return (sun.reflect.annotation.AnnotationInvocationHandler)var2;
            }
        }

        return null;
    }

    private static boolean memberValueEquals(Object var0, Object var1) {
        Class var2 = var0.getClass();
        if (!var2.isArray()) {
            return var0.equals(var1);
        } else if (var0 instanceof Object[] && var1 instanceof Object[]) {
            return Arrays.equals((Object[])((Object[])var0), (Object[])((Object[])var1));
        } else if (var1.getClass() != var2) {
            return false;
        } else if (var2 == byte[].class) {
            return Arrays.equals((byte[])((byte[])var0), (byte[])((byte[])var1));
        } else if (var2 == char[].class) {
            return Arrays.equals((char[])((char[])var0), (char[])((char[])var1));
        } else if (var2 == double[].class) {
            return Arrays.equals((double[])((double[])var0), (double[])((double[])var1));
        } else if (var2 == float[].class) {
            return Arrays.equals((float[])((float[])var0), (float[])((float[])var1));
        } else if (var2 == int[].class) {
            return Arrays.equals((int[])((int[])var0), (int[])((int[])var1));
        } else if (var2 == long[].class) {
            return Arrays.equals((long[])((long[])var0), (long[])((long[])var1));
        } else if (var2 == short[].class) {
            return Arrays.equals((short[])((short[])var0), (short[])((short[])var1));
        } else {
            assert var2 == boolean[].class;

            return Arrays.equals((boolean[])((boolean[])var0), (boolean[])((boolean[])var1));
        }
    }

    private Method[] getMemberMethods() {
        if (this.memberMethods == null) {
            this.memberMethods = (Method[])AccessController.doPrivileged(new PrivilegedAction<Method[]>() {
                public Method[] run() {
                    Method[] var1 = sun.reflect.annotation.AnnotationInvocationHandler.this.type.getDeclaredMethods();
                    sun.reflect.annotation.AnnotationInvocationHandler.this.validateAnnotationMethods(var1);
                    AccessibleObject.setAccessible(var1, true);
                    return var1;
                }
            });
        }

        return this.memberMethods;
    }

    private void validateAnnotationMethods(Method[] var1) {
        boolean var2 = true;
        Method[] var3 = var1;
        int var4 = var1.length;
        int var5 = 0;

        while(var5 < var4) {
            Method var6 = var3[var5];
            if (var6.getModifiers() == 1025 && !var6.isDefault() && var6.getParameterCount() == 0 && var6.getExceptionTypes().length == 0) {
                Class var7 = var6.getReturnType();
                if (var7.isArray()) {
                    var7 = var7.getComponentType();
                    if (var7.isArray()) {
                        var2 = false;
                        break;
                    }
                }

                if ((!var7.isPrimitive() || var7 == Void.TYPE) && var7 != String.class && var7 != Class.class && !var7.isEnum() && !var7.isAnnotation()) {
                    var2 = false;
                    break;
                }

                String var8 = var6.getName();
                if ((!var8.equals("toString") || var7 != String.class) && (!var8.equals("hashCode") || var7 != Integer.TYPE) && (!var8.equals("annotationType") || var7 != Class.class)) {
                    ++var5;
                    continue;
                }

                var2 = false;
                break;
            }

            var2 = false;
            break;
        }

        if (!var2) {
            throw new AnnotationFormatError("Malformed method on an annotation type");
        }
    }

    private int hashCodeImpl() {
        int var1 = 0;

        Entry var3;
        for(Iterator var2 = this.memberValues.entrySet().iterator(); var2.hasNext(); var1 += 127 * ((String)var3.getKey()).hashCode() ^ memberValueHashCode(var3.getValue())) {
            var3 = (Entry)var2.next();
        }

        return var1;
    }

    private static int memberValueHashCode(Object var0) {
        Class var1 = var0.getClass();
        if (!var1.isArray()) {
            return var0.hashCode();
        } else if (var1 == byte[].class) {
            return Arrays.hashCode((byte[])((byte[])var0));
        } else if (var1 == char[].class) {
            return Arrays.hashCode((char[])((char[])var0));
        } else if (var1 == double[].class) {
            return Arrays.hashCode((double[])((double[])var0));
        } else if (var1 == float[].class) {
            return Arrays.hashCode((float[])((float[])var0));
        } else if (var1 == int[].class) {
            return Arrays.hashCode((int[])((int[])var0));
        } else if (var1 == long[].class) {
            return Arrays.hashCode((long[])((long[])var0));
        } else if (var1 == short[].class) {
            return Arrays.hashCode((short[])((short[])var0));
        } else {
            return var1 == boolean[].class ? Arrays.hashCode((boolean[])((boolean[])var0)) : Arrays.hashCode((Object[])((Object[])var0));
        }
    }

    private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
        GetField var2 = var1.readFields();
        Class var3 = (Class)var2.get("type", (Object)null);
        Map var4 = (Map)var2.get("memberValues", (Object)null);
        AnnotationType var5 = null;

        try {
            var5 = AnnotationType.getInstance(var3);
        } catch (IllegalArgumentException var13) {
            throw new InvalidObjectException("Non-annotation type in annotation serial stream");
        }

        Map var6 = var5.memberTypes();
        LinkedHashMap var7 = new LinkedHashMap();

        String var10;
        Object var11;
        for(Iterator var8 = var4.entrySet().iterator(); var8.hasNext(); var7.put(var10, var11)) {
            Entry var9 = (Entry)var8.next();
            var10 = (String)var9.getKey();
            var11 = null;
            Class var12 = (Class)var6.get(var10);
            if (var12 != null) {
                var11 = var9.getValue();
                if (!var12.isInstance(var11) && !(var11 instanceof ExceptionProxy)) {
                    var11 = (new AnnotationTypeMismatchExceptionProxy(var11.getClass() + "[" + var11 + "]")).setMember((Method)var5.members().get(var10));
                }
            }
        }

        sun.reflect.annotation.AnnotationInvocationHandler.UnsafeAccessor.setType(this, var3);
        sun.reflect.annotation.AnnotationInvocationHandler.UnsafeAccessor.setMemberValues(this, var7);
    }

    private static class UnsafeAccessor {
        private static final Unsafe unsafe;
        private static final long typeOffset;
        private static final long memberValuesOffset;

        private UnsafeAccessor() {
        }

        static void setType(sun.reflect.annotation.AnnotationInvocationHandler var0, Class<? extends Annotation> var1) {
            unsafe.putObject(var0, typeOffset, var1);
        }

        static void setMemberValues(sun.reflect.annotation.AnnotationInvocationHandler var0, Map<String, Object> var1) {
            unsafe.putObject(var0, memberValuesOffset, var1);
        }

        static {
            try {
                unsafe = Unsafe.getUnsafe();
                typeOffset = unsafe.objectFieldOffset(sun.reflect.annotation.AnnotationInvocationHandler.class.getDeclaredField("type"));
                memberValuesOffset = unsafe.objectFieldOffset(sun.reflect.annotation.AnnotationInvocationHandler.class.getDeclaredField("memberValues"));
            } catch (Exception var1) {
                throw new ExceptionInInitializerError(var1);
            }
        }
    }
}
