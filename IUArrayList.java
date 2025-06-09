import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Array-based implementation of IndexedUnsortedList.
 * An Iterator with working remove() method is implemented, but
 * ListIterator is unsupported. 
 * 
 * @author Caleb Corlewski
 *
 * @param <T> type to store
 */
public class IUArrayList<T> implements IndexedUnsortedList<T> {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int NOT_FOUND = -1;
	
	private T[] array;
	private int rear;
	private int modCount;
	
	/** Creates an empty list with default initial capacity */
	public IUArrayList() {
		this(DEFAULT_CAPACITY);
	}
	
	/** 
	 * Creates an empty list with the given initial capacity
	 * @param initialCapacity
	 */
	@SuppressWarnings("unchecked")
	public IUArrayList(int initialCapacity) {
		array = (T[])(new Object[initialCapacity]);
		rear = 0;
		modCount = 0;
	}
	
	/** Double the capacity of array */
	private void expandCapacity() {
		array = Arrays.copyOf(array, array.length*2);
	}

    /*  
     * Adds the specified element to the front of this list. 
     *
     * @param element the element to be added to the front of this list    
     */
	@Override
	public void addToFront(T element) {
		if ( rear == array.length)
		{
			expandCapacity();
		}
		for (int i = rear; i > 0; i--) 
		{
    		array[i] = array[i - 1];
		}
		array[0] = element;
		rear++;
		modCount++;		
	}

    /*
     * Adds the specified element to the rear of this list. 
     *
     * @param element the element to be added to the rear of this list    
     */
	@Override
	public void addToRear(T element) 
	{
		if ( rear == array.length)
		{
			expandCapacity();
		}
		array[rear] = element;
		rear++;
		modCount++;
		
	}

    /*  
     * Adds the specified element to the rear of this list. 
     *
     * @param element  the element to be added to the rear of the list    
     */
	@Override
	public void add(T element) {
		addToRear(element); 
	}

    /* 
     * Adds the specified element after the first element of the list matching the specified target. 
     *
     * @param element the element to be added after the target
     * @param target  the target is the item that the element will be added after
     * @throws NoSuchElementException if target element is not in this list
     */
	@Override
	public void addAfter(T element, T target) 
	{
		int index = 0;
		Boolean NOTFOUND = true;
		while(NOTFOUND && index < rear)
		{
			if (array[index].equals(target))
			{
				NOTFOUND = false;
			}
			index++;
		}
		if(NOTFOUND)
		{
			throw new NoSuchElementException();
		}
		add(index, element);
		
	}

    /*  
     * Inserts the specified element at the specified index. 
     * 
     * @param index   the index into the array to which the element is to be inserted.
     * @param element the element to be inserted into the array
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index > size)
     */
	@Override
	public void add(int index, T element) {
		if(index < 0 || index > rear)
        {
            throw new IndexOutOfBoundsException();
        }
		if (rear == array.length)
		{
			expandCapacity();
		}
		for (int i = rear; i > index; i--)
		{
        array[i] = array[i - 1];
    	}
		array[index] = element;
		rear++;
		modCount++;
	}

    /*
     * Removes and returns the first element from this list. 
     * 
     * @return the first element from this list
     * @throws NoSuchElementException if list contains no elements
     */
	@Override
	public T removeFirst() 
	{
		if(rear == 0)
		{
			throw new NoSuchElementException();
		}
		T retVal = array[0];
		//shift elements after index 0 forward 1
		for(int i = 0; i < rear - 1; i++)
        {
            array[i] = array [i + 1];
        }
		array[rear - 1] = null;
		rear--;
		modCount++;
		return retVal;
	}

	/*  
     * Removes and returns the last element from this list. 
     *
     * @return the last element from this list
     * @throws NoSuchElementException if list contains no elements
     */
    @Override
	public T removeLast() 
	{
		if(rear == 0)
		{
			throw new NoSuchElementException();
		}
		T retVal = array[rear - 1];
		array[rear - 1] = null;
		rear--;
		modCount++;
		return retVal;
	}

	/*
     * Removes and returns the first element from the list matching the specified element.
     *
     * @param element the element to be removed from the list
     * @return removed element
     * @throws NoSuchElementException if element is not in this list
     */
    @Override
	public T remove(T element) 
    {
		int index = indexOf(element);
		if (index == NOT_FOUND) {
			throw new NoSuchElementException();
		}
		
		T retVal = array[index];
		
		rear--;
		//shift elements
		for (int i = index; i < rear; i++) {
			array[i] = array[i+1];
		}
		array[rear] = null;
		modCount++;
		
		return retVal;
	}

     /*
     * Removes and returns the element at the specified index. 
     *
     * @param index the index of the element to be retrieved
     * @return the element at the given index
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
	@Override
	public T remove(int index) 
	{
        if(index < 0 || index >= rear)
        {
            throw new IndexOutOfBoundsException();
        }
		T retVal = array[index];
        //shift elements after index forward 1
        for(int i = index; i < rear - 1; i++)
        {
            array[i] = array [i + 1];
        }
        array[rear - 1] = null;
        rear--;
        modCount++;
		return retVal;
	}

    /*
     * Replace the element at the specified index with the given element. 
     *
     * @param index   the index of the element to replace
     * @param element the replacement element to be set into the list
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
	@Override
	public void set(int index, T element) 
    {
		if(index < 0 || index >= rear)
        {
            throw new IndexOutOfBoundsException();
        }
        array[index] = element;
        modCount++;
		
	}

    /* 
     * Returns a reference to the element at the specified index. 
     *
     * @param index  the index to which the reference is to be retrieved from
     * @return the element at the specified index
     * @throws IndexOutOfBoundsException if the index is out of range (index < 0 || index >= size)
     */
	@Override
	public T get(int index) 
    {
		if(index < 0 || index >= rear)
        {
            throw new IndexOutOfBoundsException();
        }
		return array[index];
	}

    /*  
     * Returns the index of the first element from the list matching the specified element. 
     *
     * @param element  the element for the index is to be retrieved
     * @return the integer index for this element or -1 if element is not in the list    
     */
	@Override
	public int indexOf(T element) 
    {
		int index = NOT_FOUND;
		
		if (!isEmpty()) {
			int i = 0;
			while (index == NOT_FOUND && i < rear) {
				if (element.equals(array[i])) {
					index = i;
				} else {
					i++;
				}
			}
		}
		
		return index;
	}

    /* 
     * Returns a reference to the first element in this list. 
     *
     * @return a reference to the first element in this list
     * @throws NoSuchElementException if list contains no elements
     */
	@Override
	public T first() 
    {
        if(rear == 0)
        {
            throw new NoSuchElementException();
        } 
		return array[0];
	}

    /*
     * Returns a reference to the last element in this list. 
     *
     * @return a reference to the last element in this list
     * @throws NoSuchElementException if list contains no elements
     */
	@Override
	public T last() 
    {
        if(rear == 0)
        {
            throw new NoSuchElementException();
        }
		return array[rear-1];
	}

    /*  
     * Returns true if this list contains the specified target element. 
     *
     * @param target the target that is being sought in the list
     * @return true if the list contains this element, else false
     */
	@Override
	public boolean contains(T target) 
    {
		return (indexOf(target) != NOT_FOUND);
	}

    /** 
     * Returns true if this list contains no elements. 
     *
     * @return true if this list contains no elements
     */
	@Override
	public boolean isEmpty()
    {
		return rear == 0;
	}

    /* 
     * Returns the number of elements in this list. 
     *
     * @return the integer representation of number of elements in this list
     */
	@Override
	public int size() 
    {
		return rear;
	}
     
    /* 
     * Returns a string representation of this list. 
     *
     * @return a string representation of this list
     */
    public String toString()
    {
        StringBuilder string = new StringBuilder("[");
		for(int i = 0; i < rear; i++)
		{
			if(i< rear -1){
				string.append(array[i]);
				string.append(",");
			}else
			{
				string.append(array[i]);
			}
		}
		string.append("]");
		
		return string.toString();
    }

	@Override
	public Iterator<T> iterator() {
		return new ALIterator();
	}

	@Override
	public ListIterator<T> listIterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ListIterator<T> listIterator(int startingIndex) {
		throw new UnsupportedOperationException();
	}

	/** Iterator for IUArrayList */
	private class ALIterator implements Iterator<T> 
	{
		private int nextIndex;
		private int iterModCount;
		private boolean canRemove;
		
		public ALIterator() 
		{
			nextIndex = 0;
			iterModCount = modCount;
			canRemove = false;
		}
		/*
		 * Checks if the iterator has a valid next object
		 * @returns boolean true if there is a next value
		 */
		@Override
		public boolean hasNext() 
		{
			 if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			return nextIndex < rear;
		}

		/*
		 * Moves the iterator forward 1 step.
		 * @returns the value of the previous element
		 */
		@Override
		public T next() 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			if(!hasNext())
			{
				throw new NoSuchElementException();
			}
			canRemove = true;
			return array[nextIndex++];
		}
		
		@Override
		public void remove() 
		{
			if(iterModCount != modCount)
			{
				throw new ConcurrentModificationException();
			}
			if(!canRemove)
			{
				throw new IllegalStateException();
			}
			
			for (int i = nextIndex - 1; i < rear - 1; i++)
			{
            array[i] = array[i + 1];
        	}
       		array[--rear] = null;
        	modCount++;
        	iterModCount++;
        	nextIndex--;
        	canRemove = false;
			
		}
	}
}