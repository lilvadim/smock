package smock.external.byteBuddy;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bytecode.assign.Assigner;
import smock.exception.SmockException;
import smock.internal.CallerIdentifier;
import smock.internal.MethodDispatcher;

import java.lang.reflect.Method;

public class ByteBuddyMethodDispatcherAdapterJ {
    @Advice.OnMethodEnter
    public static Object interceptRedefined(
            @Advice.This Object obj,
            @Advice.Origin Method method,
            @Advice.AllArguments Object[] args
    ) {
        return MethodDispatcher.delegate(new CallerIdentifier(obj, null), obj, method, args, null);
    }

    @Advice.OnMethodExit(onThrowable = SmockException.class)
    public static void exit(
            @Advice.Enter Object enter,
            @Advice.Return(readOnly = false, typing = Assigner.Typing.DYNAMIC) Object returnValue,
            @Advice.Thrown SmockException exception
    ) {
        returnValue = enter;
    }
}
