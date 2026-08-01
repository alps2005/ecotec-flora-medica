# Reglas R8/ProGuard para el build release.

# --- kotlinx.serialization ---
# Mantener los serializadores generados para las @Serializable data classes,
# si no R8 los elimina y el parseo de content.json fallaría en runtime.
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.**

-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}

# Modelos de la app anotados con @Serializable
-keep,includedescriptorclasses class com.ecotec.floramedica.data.model.**$$serializer { *; }
-keepclassmembers class com.ecotec.floramedica.data.model.** {
    *** Companion;
    kotlinx.serialization.KSerializer serializer(...);
}
-keep class com.ecotec.floramedica.data.model.** { *; }
