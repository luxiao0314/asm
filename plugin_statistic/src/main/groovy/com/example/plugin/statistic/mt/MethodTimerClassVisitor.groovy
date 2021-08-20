package com.example.plugin.statistic.mt

import com.example.plugin.statistic.StatisticPlugin
import org.objectweb.asm.*

class MethodTimerClassVisitor extends ClassVisitor {

    String className

    MethodTimerClassVisitor(ClassVisitor classVisitor) {
        super(Opcodes.ASM7, classVisitor)
    }

    /**
     * @param version 类版本
     * @param access 修饰符
     * @param name 类名
     * @param signature 泛型信息
     * @param superName 父类
     * @param interfaces 实现的接口
     */
    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        super.visit(version, access, name, signature, superName, interfaces)
        this.className = name
    }

    @Override
    void visitEnd() {
        super.visitEnd()
    }

    /**
     * 扫描类的方法进行调用
     * @param access 修饰符
     * @param name 方法名字
     * @param descriptor 方法签名
     * @param signature 泛型信息
     * @param exceptions 抛出的异常
     * @return
     */
    @Override
    MethodVisitor visitMethod(int methodAccess, String methodName, String methodDescriptor, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(methodAccess, methodName, methodDescriptor, signature, exceptions)
        if ((methodAccess & Opcodes.ACC_INTERFACE) == 0 && "<init>" != methodName && "<clinit>" != methodName) {
            if (StatisticPlugin.statisticExtension.time == -1) {
                methodVisitor = new MethodTimerAdapter(api, methodVisitor, className, methodAccess, methodName, methodDescriptor)
            } else {
                methodVisitor = new MethodTimerAdviceAdapter(api, methodVisitor, className, methodAccess, methodName, methodDescriptor)
            }
        }
        return methodVisitor
    }
}

