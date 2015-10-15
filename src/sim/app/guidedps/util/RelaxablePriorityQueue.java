package sim.app.guidedps.util;

import java.util.HashMap;
import java.util.PriorityQueue;

public class RelaxablePriorityQueue<T> {
	private HashMap<T, Double> priorityMap = new HashMap<T, Double>();
	private PriorityQueue<T> queue = new PriorityQueue<T>();
	
	public RelaxablePriorityQueue() {
		
	}
	
	public boolean add(T tuple, double priority) throws Exception {
		if(priorityMap.containsKey(tuple))
			throw new Exception("current tuple already exists");
			
		priorityMap.put(tuple, priority);
		return queue.add(tuple);
	}
	
	public int size() throws Exception {
		if(this.queue.size()!=this.priorityMap.size())
			throw new Exception("size unmatched");
		return this.queue.size();
	}
	
	public T peek() {
		return this.queue.peek();
	}
	
	public T poll() {
		T tuple = this.queue.poll();
		priorityMap.remove(tuple);
		return tuple;
	}
	
	
	public boolean relax(T tuple, double priority) throws Exception {
		if(!priorityMap.containsKey(tuple)) {
			return add(tuple, priority);
		}
		else {
			double originalPriority = priorityMap.get(tuple);
			if(originalPriority < priority) {
				queue.remove(tuple);
				priorityMap.put(tuple, priority);
				queue.add(tuple);
			}
			return true;
		}
	}
	
	public void clear() {
		queue.clear();
		priorityMap.clear();
	}

	
	public boolean isEmpty() throws Exception {
		if(priorityMap.isEmpty()&&queue.isEmpty())
			return true;
		else if(!priorityMap.isEmpty()&&!queue.isEmpty()) 
			return false;
		else
			throw new Exception("Unmatched tuple numbers");
	}
	
}
