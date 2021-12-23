package pl.andrzej.shop.function;

public interface BiConsumerThrowable<V,T,X extends Throwable> {

    void accept(V value,T type) throws X;

}
