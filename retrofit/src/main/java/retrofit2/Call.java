/*
 * Copyright (C) 2015 Square, Inc.
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

import java.io.IOException;
import okhttp3.Request;

/**
 * Retrofit方法的一次调用————给Webserver发送一个request并返回一个response。
 * An invocation of a Retrofit method that sends a request to a webserver and returns a response.
 *
 * 每个请求都产生自己的http请求和响应对.(yield:产生，生产)
 * Each call yields its own HTTP request and response pair. Use {@link #clone} to make multiple
 * calls with the same parameters to the same webserver; this may be used to implement polling or
 * to retry a failed call.
 *
 * <p>Calls may be executed synchronously with {@link #execute}, or asynchronously with {@link
 * #enqueue}. In either case the call can be canceled at any time with {@link #cancel}. A call that
 * is busy writing its request or reading its response may receive a {@link IOException}; this is
 * working as designed.
 *
 * @param <T> Successful response body type.(请求成功下的响应体{Response Body}类型)
 */
public interface Call<T> extends Cloneable {
  /**
   * 同步发送请求并返回Response
   * Synchronously send the request and return its response.
   *
   * @throws IOException if a problem occurred talking to the server.
   * @throws RuntimeException (and subclasses) if an unexpected error occurs creating the request
   * or decoding the response.
   */
  Response<T> execute() throws IOException;

  /**
   * 异步发送请求，使用Callback来通知它的Response。或者是If在与服务器对话中发生错误，创建请求OR处理Response.
   * Asynchronously send the request and notify {@code callback} of its response or if an error
   * occurred talking to the server, creating the request, or processing the response.
   */
  void enqueue(Callback<T> callback);

  /**
   * Returns true if this call has been either {@linkplain #execute() executed} or {@linkplain
   * #enqueue(Callback) enqueued}. It is an error to execute or enqueue a call more than once.
   */
  boolean isExecuted();

  /**
   * in-flight 正在进行中的
   * Cancel this call. An attempt will be made to cancel in-flight calls, and if the call has not
   * yet been executed it never will be.
   */
  void cancel();

  /** True if {@link #cancel()} was called. */
  boolean isCanceled();

  /**
   * identical(相同的，完全一致的)
   * Create a new, identical call to this one which can be enqueued or executed even if this call
   * has already been.
   */
  Call<T> clone();

  /** The original HTTP request. */
  Request request();
}
