public class Util {
  void foo(int labInt) {
    label:
    while (true) {
      break l<caret>
    }
  }
}
