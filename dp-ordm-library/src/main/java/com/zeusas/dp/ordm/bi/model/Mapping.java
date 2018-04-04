package com.zeusas.dp.ordm.bi.model;

import java.io.Serializable;
import java.util.Collection;

public interface Mapping<T, PK> extends Serializable {
	void build(Collection<T> cc);

	T get(int idx);

	Integer index(PK pk);

	int size();

	void clear();

	T[] mapping();
}
