-dontwarn org.bouncycastle.jsse.BCSSLParameters
-dontwarn org.bouncycastle.jsse.BCSSLSocket
-dontwarn org.bouncycastle.jsse.provider.BouncyCastleJsseProvider
-dontwarn org.conscrypt.Conscrypt$Version
-dontwarn org.conscrypt.Conscrypt
-dontwarn org.conscrypt.ConscryptHostnameVerifier
-dontwarn org.openjsse.javax.net.ssl.SSLParameters
-dontwarn org.openjsse.javax.net.ssl.SSLSocket
-dontwarn org.openjsse.net.ssl.OpenJSSE

# Fix for Retrofit issue https://github.com/square/retrofit/issues/3751
# Keep generic signature of Call, Response (R8 full mode strips signatures from non-kept items).
# With R8 full mode generic signatures are stripped for classes that are not
# kept. Suspend functions are wrapped in continuations where the type argument
# is used.
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# https://stackoverflow.com/questions/70037537/proguard-missing-classes-detected-while-running-r8-after-adding-package-names-in
-dontwarn com.sun.jna.FunctionMapper
-dontwarn com.sun.jna.JNIEnv
-dontwarn com.sun.jna.Library
-dontwarn com.sun.jna.Native
-dontwarn com.sun.jna.Platform
-dontwarn com.sun.jna.Pointer
-dontwarn com.sun.jna.Structure
-dontwarn com.sun.jna.platform.win32.Kernel32
-dontwarn com.sun.jna.platform.win32.Win32Exception
-dontwarn com.sun.jna.platform.win32.WinDef$LPVOID
-dontwarn com.sun.jna.platform.win32.WinNT$HANDLE
-dontwarn com.sun.jna.win32.StdCallLibrary
-dontwarn com.sun.jna.win32.W32APIOptions
-dontwarn edu.umd.cs.findbugs.annotations.SuppressFBWarnings
-dontwarn java.lang.instrument.ClassDefinition
-dontwarn java.lang.instrument.IllegalClassFormatException
-dontwarn java.lang.instrument.UnmodifiableClassException
-dontwarn org.apiguardian.api.API$Status
-dontwarn org.apiguardian.api.API
