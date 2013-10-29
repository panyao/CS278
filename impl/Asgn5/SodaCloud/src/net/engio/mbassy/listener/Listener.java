/******************************************************************************************
MIT License

Copyright (c) 2012 Benjamin Diedrichsen

Permission is hereby granted, free of charge, to any person obtaining a copy of this
software and associated documentation files (the "Software"), to deal in the Software
without restriction, including without limitation the rights to use, copy, modify, merge,
publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or
substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
DEALINGS IN THE SOFTWARE.
********************************************************************************************/
package net.engio.mbassy.listener;

import java.lang.annotation.*;

/**
 * Mark any method of any object as a message handler and configure the handler
 * using different properties.
 *
 * @author bennidi
 * Date: 2/8/12
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Inherited
@Target(value = {ElementType.METHOD})
public @interface Listener {


	Filter[] filters() default {}; // no filters by default

    Mode delivery() default Mode.Sequential;

    int priority() default 0;

    boolean rejectSubtypes() default false;

    boolean enabled() default true;

}
