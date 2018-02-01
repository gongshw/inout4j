# inout4j
@In and @Out annotation on parameters for java

[![Travis](https://img.shields.io/travis/gongshw/inout4j.svg?style=flat-square)](https://travis-ci.org/gongshw/inout4j)
[![Coveralls](https://img.shields.io/coveralls/github/gongshw/inout4j.svg?style=flat-square)](https://coveralls.io/github/gongshw/inout4j)

## Usage

```java
import com.gongshw.inout4j.annotation.ReadOnly;
import com.gongshw.inout4j.annotation.Writable;

class Inout4jDemo {
    private Object field = null;

    @ReadOnly
    private void update() {
        new Runnable() {
            @Override
            public void run() {
                this.field = new Object(); // compile error!!!
            }
        };
    }

    @Writable
    private void update(int param) {
    }

    @ReadOnly
    private Object get() {
        System.out.print("get something");
        this.update(0); // compile error!!!
        update(0); // compile error!!!
        return this.field;
    }

    @Writable
    public void set() {

    }

    @ReadOnly
    private void getInvokeSet() {
        this.set(0); // compile error!!!
    }
}
```
