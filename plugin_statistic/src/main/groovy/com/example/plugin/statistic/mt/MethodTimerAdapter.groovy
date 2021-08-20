package com.example.plugin.statistic.mt

import com.example.plugin.statistic.StatisticPlugin
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

import java.util.regex.Pattern

/**
 * 调用外部方法,自己实现打印
 */
class MethodTimerAdapter extends AdviceAdapter {

    String className
    String methodName
    String desc
    String impl = StatisticPlugin.statisticExtension.impl.replace(".", "/")
    boolean isNull = false

    MethodTimerAdapter(int api, MethodVisitor methodVisitor, String owner, int access, String name, String desc) {
        super(api, methodVisitor, access, name, desc)
        this.className = owner
        this.methodName = name
        this.desc = desc

        def isInit = name == "<init>"
        def isStaticInit = name == "<clinit>"
        def isStatic = (access & ACC_STATIC) != 0

        isNull = isStatic || isInit || isStaticInit
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        if (isInject(className)) {

            if (isNull) {
                mv.visitInsn(ACONST_NULL)//null
            } else {
                mv.visitVarInsn(ALOAD, 0)//this
            }

            mv.visitLdcInsn(className)//className
            mv.visitLdcInsn(methodName)//methodbName
            mv.visitLdcInsn(getArgsType(Type.getArgumentTypes(desc)))//argsTypes
            mv.visitLdcInsn(returnType.className)//returntype
            mv.visitInsn(ICONST_0)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
            mv.visitMethodInsn(INVOKESTATIC, impl, "enter",
                    "(" +
                            "Ljava/lang/Object;" +
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "[Ljava/lang/Object;" +
                            ")V",
                    false)
        }
    }

    @Override
    void onMethodExit(int opcode) {
        if (isInject(className)) {

            if (isNull) {
                mv.visitInsn(ACONST_NULL)//null
            } else {
                mv.visitVarInsn(ALOAD, 0)//this
            }

            mv.visitLdcInsn(className)//className
            mv.visitLdcInsn(methodName)//methodbName
            mv.visitLdcInsn(getArgsType(Type.getArgumentTypes(desc)))//argsTypes
            mv.visitLdcInsn(returnType.className)//returntype
            mv.visitInsn(ICONST_0)
            mv.visitTypeInsn(ANEWARRAY, "java/lang/Object")
            mv.visitMethodInsn(INVOKESTATIC, impl, "exit",
                    "(" +
                            "Ljava/lang/Object;" + //this
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "Ljava/lang/String;" +
                            "[Ljava/lang/Object;" +//prams
                            ")V",
                    false)
        }
        super.onMethodExit(opcode)
    }

    static String getArgsType(argsTypes) {
        if (argsTypes == null)
            return "null"

        int iMax = argsTypes.length - 1
        if (iMax == -1)
            return "[]"

        StringBuilder b = new StringBuilder()
        b.append('[')
        for (int i = 0; ; i++) {
            b.append(String.valueOf(argsTypes[i].className))
            if (i == iMax)
                return b.append(']').toString()
            b.append(", ")
        }
    }

    //优先级 是否启用，注解，类名白名单
    static boolean isInject(String className) {
        for (String value : StatisticPlugin.statisticExtension.classWhiteListRegex) {
            if (className.contains(value)) {
                return true
            }
        }
        return false
    }
}
