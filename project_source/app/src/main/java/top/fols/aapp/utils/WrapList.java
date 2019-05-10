package top.fols.aapp.utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
public class WrapList<E extends Object> implements List<E> {
	private List<E> list;
	public WrapList() {
		this.list = new ArrayList<>();
	}
	public void setList(List<E> list) {
		this.list = list == null ?new ArrayList<>(): list;
	}
	public List<E> getList() {
		return this.list == null ?new ArrayList<>(): list;
	}
	
	
	
	@Override
	public int size() {
		// TODO: Implement this method
		return list.size();
	}

	@Override
	public boolean isEmpty() {
		// TODO: Implement this method
		return list.isEmpty();
	}

	@Override
	public boolean contains(Object p1) {
		// TODO: Implement this method
		return list.contains(p1);
	}

	@Override
	public Iterator<E> iterator() {
		// TODO: Implement this method
		return list.iterator();
	}

	@Override
	public Object[] toArray() {
		// TODO: Implement this method
		return list.toArray();
	}

	@Override
	public <T extends Object> T[] toArray(T[] p1) {
		// TODO: Implement this method
		return list.toArray(p1);
	}

	@Override
	public boolean add(E p1) {
		// TODO: Implement this method
		return list.add(p1);
	}

	@Override
	public boolean remove(Object p1) {
		// TODO: Implement this method
		return list.remove(p1);
	}

	@Override
	public boolean containsAll(Collection<?> p1) {
		// TODO: Implement this method
		return list.containsAll(p1);
	}

	@Override
	public boolean addAll(Collection<? extends E> p1) {
		// TODO: Implement this method
		return list.addAll(p1);
	}

	@Override
	public boolean addAll(int p1, Collection<? extends E> p2) {
		// TODO: Implement this method
		return list.addAll(p1, p2);
	}

	@Override
	public boolean removeAll(Collection<?> p1) {
		// TODO: Implement this method
		return list.removeAll(p1);
	}

	@Override
	public boolean retainAll(Collection<?> p1) {
		// TODO: Implement this method
		return list.retainAll(p1);
	}

	@Override
	public void clear() {
		// TODO: Implement this method
		list.clear();
	}

	@Override
	public boolean equals(Object p1) {
		// TODO: Implement this method
		return list.equals(p1);
	}

	@Override
	public int hashCode() {
		// TODO: Implement this method
		return list.hashCode();
	}

	@Override
	public E get(int p1) {
		// TODO: Implement this method
		return list.get(p1);
	}

	@Override
	public E set(int p1, E p2) {
		// TODO: Implement this method
		return list.set(p1, p2);
	}

	@Override
	public void add(int p1, E p2) {
		// TODO: Implement this method
		list.add(p1, p2);
	}

	@Override
	public E remove(int p1) {
		// TODO: Implement this method
		return list.remove(p1);
	}

	@Override
	public int indexOf(Object p1) {
		// TODO: Implement this method
		return list.indexOf(p1);
	}

	@Override
	public int lastIndexOf(Object p1) {
		// TODO: Implement this method
		return list.lastIndexOf(p1);
	}

	@Override
	public ListIterator<E> listIterator() {
		// TODO: Implement this method
		return list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int p1) {
		// TODO: Implement this method
		return list.listIterator();
	}

	@Override
	public List<E> subList(int p1, int p2) {
		// TODO: Implement this method
		return list.subList(p1, p2);
	}


}
