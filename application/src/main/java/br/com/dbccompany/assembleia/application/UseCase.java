package br.com.dbccompany.assembleia.application;

public abstract class UseCase<IN, OUT> {
    public abstract OUT execute(IN anIn);
}
