/*
 * Copyright (C) 2018 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * (Retrofit服务接口方法的一个单独调用。这个类用来捕捉被调用的方法以及该方法的参数)
 *
 * A single invocation of a Retrofit service interface method. This class captures both the method
 * that was called and the arguments to the method.
 *
 * Retrofit自动地为每个OkHttp请求添加一个tag.你可以在OkHttp用于测量&监视的拦截器中检索此次调用。
 * <p>Retrofit automatically adds an invocation to each OkHttp request as a tag. You can retrieve
 * the invocation in an OkHttp interceptor for metrics and monitoring.
 *
 * <pre><code>
 * class InvocationLogger implements Interceptor {
 *   &#64;Override public Response intercept(Chain chain) throws IOException {
 *     Request request = chain.request();
 *     Invocation invocation = request.tag(Invocation.class);
 *     if (invocation != null) {
 *       System.out.printf("%s.%s %s%n",
 *           invocation.method().getDeclaringClass().getSimpleName(),
 *           invocation.method().getName(), invocation.arguments());
 *     }
 *     return chain.proceed(request);
 *   }
 * }
 * </code></pre>
 *
 * <strong>Note:</strong> use caution when examining an invocation's arguments. Although the
 * arguments list is unmodifiable, the arguments themselves may be mutable. They may also be unsafe
 * for concurrent access. For best results declare Retrofit service interfaces using only immutable
 * types for parameters!
 *
 * Invocation ：调用
 */
public final class Invocation {
  public static Invocation of(Method method, List<?> arguments) {
    Objects.requireNonNull(method, "method == null");
    Objects.requireNonNull(arguments, "arguments == null");
    return new Invocation(method, new ArrayList<>(arguments)); // Defensive copy.
  }

  private final Method method;
  private final List<?> arguments;

  /** Trusted constructor assumes ownership of {@code arguments}. */
  Invocation(Method method, List<?> arguments) {
    this.method = method;
    this.arguments = Collections.unmodifiableList(arguments);
  }

  public Method method() {
    return method;
  }

  public List<?> arguments() {
    return arguments;
  }

  @Override public String toString() {
    return String.format("%s.%s() %s",
        method.getDeclaringClass().getName(), method.getName(), arguments);
  }
}
