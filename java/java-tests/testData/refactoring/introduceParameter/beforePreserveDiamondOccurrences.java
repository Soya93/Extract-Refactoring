
public class TestCompletion {

    public static <T, V> ParallelPipeline<T, V> test(T base, V newStage, T upstream) {
        if (base != null){
            return <selection>new ParallelPipeline<>(base, newStage)</selection>;
        }
        else {
            return new ParallelPipeline<>(upstream, newStage);
        }

    }


    void f() {
        test(null, null, null);
    }
    private static class ParallelPipeline<T, V> {
        public ParallelPipeline(T p0, V p1) {
        }
    }
}
