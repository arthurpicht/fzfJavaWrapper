# fzfJavaWrapper
A java wrapper around [fzf](https://github.com/junegunn/fzf). Makes fzf available for command line
interfaces written in java.

Tested for Linux only.

### Example:

```java
Fzf fzf = Fzf.builder()
        .withPrompt("Input>")
        .withTac()
        .withNoSort()
        .build();

List<String> elements = List.of("element1", "element2", "element3");
String result = fzf.execute(elements);

System.out.println("Your choice: " + result);
```
See the `FzfBuilder` class for all configuration options.

### Restriction:

Input elements and chosen results are cached in temporary files located in the 
home directory of the user. Respective files are deleted immediately
after performing fzf native binary. Hence, this functionality should
not be used for sensible data.

