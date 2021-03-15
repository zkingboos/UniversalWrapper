package dev.king.universal.processor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.TypeElement;
import java.util.Set;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({"dev.king.universal.processor.annotation.UniversalRepository"})
public class ScopeTrace extends AbstractProcessor {

    @Override
    public boolean process(Set<? extends TypeElement> elements, RoundEnvironment environment) {
        for (TypeElement element : elements) {
            System.out.println("element.getSimpleName().toString() = " + element.getSimpleName().toString());
        }
        return false;
    }
}
