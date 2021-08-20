package com.example.plugin.statistic.mt

import com.example.plugin.statistic.StatisticPlugin
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Type
import org.objectweb.asm.commons.AdviceAdapter

/**
 * 内部直接打印方法耗时
 */
class MethodTimerAdviceAdapter extends AdviceAdapter {

    String className
    String methodName
    int slotIndex

    MethodTimerAdviceAdapter(int api, MethodVisitor methodVisitor, String owner, int access, String name, String desc) {
        super(api, methodVisitor, access, name, desc)
        this.className = owner
        this.methodName = name
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter()
        if (isInject(className)) {
            slotIndex = newLocal(Type.LONG_TYPE)
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            mv.visitVarInsn(LSTORE, slotIndex)
        }
    }

    @Override
    void onMethodExit(int opcode) {
        if (isInject(className)) {
            mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false)
            mv.visitVarInsn(LLOAD, slotIndex)
            mv.visitInsn(LSUB)
            mv.visitVarInsn(LSTORE, slotIndex)
            mv.visitVarInsn(LLOAD, slotIndex)
            mv.visitLdcInsn(new Long(StatisticPlugin.statisticExtension.time))
            mv.visitInsn(LCMP)
            Label label0 = new Label()
            mv.visitJumpInsn(IFLE, label0)
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder")
            mv.visitInsn(DUP)
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false)
            mv.visitLdcInsn(className + "/" + methodName + " --> cost time : [")
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
            mv.visitVarInsn(LLOAD, slotIndex)
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(J)Ljava/lang/StringBuilder;", false)
            mv.visitLdcInsn("ms]")
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false)
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false)
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false)
            mv.visitLabel(label0)
        }
        super.onMethodExit(opcode)
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
