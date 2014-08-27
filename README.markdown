## vraptor-biscotti-compiler

A messages.properties to Messages.java compiler.

## Why?

Because you don't want that ugly message showing to your end user. Yep, that message.

## How?

```
java -jar vraptor-biscotti-compiler.jar
```

It will read `src/main/resources/messages.properties` and save `target/i18n-classes/Messages.java` and `target/i18n-classes/MessagesDefault.java`.

## How can I deal with multiple languages
 
```
java -jar vraptor-biscotti-compiler.jar
```

If there is a default and a french version, it will generate:

```
target/i18n-classes/Messages.java
target/i18n-classes/Messages_FR.java (extends Messages.java)
```

Why does it extends Messages.java? Because it is a fallback, my boy.

# VRaptor it with panettone, please

Yes, sir:

```
java -jar vraptor-i18n-compiler.jar 
```

# VRaptor it with jsp, please

Yes, sir:

```
@Named
```

# FAQ

1. But why would I deal with `Messages`? There is a french message I want to use...
You should only use messages that are contained in all files, but if you really want to use, just receive the type you want.

2. Custom dirs?
```
java -jar vraptor-i18n-compiler.jar -i custom_messages.properties -o custom_output_folder
```

3. Is it really a good practice to output to `src/main/java` and commit it to github? I already have my `messages.properties` there.
Although it is a generated resource, it is a non-expensive one (it is small enough) so you can commit to your git repository. Feel free to write to another folder and enjoy life as you wish.

# Programmatic API

Yes, we have it:

```
new Compiler(output).compile(input);
```

You want it even more fine grained? Go for your `TypeCollector` or `MessageType`. Enjoy our test cases and use them as you wish.

# Who do I thank for being 100% sure I won't get a crappy message in my production code?

Thanks Java.

# How do I contribute?

# Where do I get help?