
public class BinarySearchTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BinarySearchTree bst = new BinarySearchTree();
		bst.insert(4);
		bst.insert(9);
		bst.insert(10);
		bst.insert(1);
		bst.insert(3);
		bst.insert(18);
		bst.insert(2);
		bst.traverse();
		System.out.println("\n------------------");
		bst.delete(4);
		bst.delete(18);
		bst.traverse();

	}

}

 class BinarySearchTree{
	 public TreeNode root;
	 private class TreeNode{
		 public int value;
		 public TreeNode leftNode;
		 public TreeNode rightNode;
		 public TreeNode(int value){
			 this.value = value;
			 this.leftNode = null;
			 this.leftNode = null;
		 }
	 }
	 public BinarySearchTree(){
		 root = null;
	 }
	 public TreeNode search(int val){
		 TreeNode p = root;
		 while(p!=null){
			 if(val==p.value)
				 return p;
			 else if(val<p.value)
				 p=p.leftNode;
			 else
				 p=p.rightNode;
		 }
		 return null;
	 }
	 public TreeNode insert(int val){
		 TreeNode n =  new TreeNode(val);
		 if(root == null){
			 root = n;
			 return n;
		 }
		 TreeNode pre = null;
		 TreeNode current =  root;
		 while(current != null){
			 pre = current;
			 if(val < current.value){
				 current = current.leftNode;
			 }
			 else{
				 current = current.rightNode;
			 }
		 }
		 if(val < pre.value)
			 pre.leftNode = n;
		 else
			 pre.rightNode =n;
		 return n;
	 }
	 
	 
	
	public TreeNode delete(int val){
		 TreeNode current = root;
		 TreeNode parent = null;
		 TreeNode result = null;
		 while(current!=null){
			 if(val ==  current.value){
				 result = current;
				 break;
			 }
			 if(val<current.value){
				 parent = current;
				 current = current.leftNode;
			 }
			 else{
				 parent = current;
				 current = current.rightNode;
			 } 
		 }
		 if(current == null)
			 return null;
		 if(current.leftNode !=null && current.rightNode != null){
			 parent = current;
			 TreeNode successor = current.rightNode;
			 while(successor.leftNode != null){
				 parent = successor;
				 successor = successor.leftNode;
			 }
			 current.value = successor.value;
			 current = successor;
		 }
		 TreeNode replaceNode = current.leftNode == null ? current.rightNode:current.leftNode;
		 if(parent!=null){
			 if(parent.leftNode == current)
				 parent.leftNode = replaceNode;
			 else
				 parent.rightNode = replaceNode;
		 }
		 else{
			 root = replaceNode;
		 }
		 current.leftNode = current.rightNode = null;
		 return result;
	 }
	 
	 public void traverse(){
		 if(root!=null)
			 helper(root);
	 }
	 private void helper(TreeNode n){
		 if(n.leftNode!=null)
			 helper(n.leftNode);
		 System.out.print(n.value+" ");
		 if(n.rightNode!=null)
			 helper(n.rightNode);
	 }
}
