package com.asm.plugin

import com.android.build.gradle.AppExtension
import com.asm.plugin.mt.MethodTimerTransform
import org.gradle.api.Plugin
import org.gradle.api.Project

class StatisticPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        def android = project.extensions.findByType(AppExtension.class)
        android.registerTransform(new MethodTimerTransform())
    }
}