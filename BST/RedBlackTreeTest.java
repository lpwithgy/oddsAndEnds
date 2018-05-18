import java.util.ArrayList;
import java.util.Iterator;


public class RedBlackTreeTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		RedBlackTree rbt = new RedBlackTree();
		rbt.insert(7);
		rbt.insert(16);
		rbt.insert(4);
		rbt.printTreeStruct();
		rbt.insert(15);
		rbt.printTreeStruct();
		rbt.insert(1);
		rbt.printTreeStruct();
		rbt.insert(11);
		rbt.printTreeStruct();
		rbt.insert(13);
		rbt.printTreeStruct();
//		rbt.delete(16);
		
//		rbt.traverse();
		rbt.delete(11);
		rbt.printTreeStruct();
		rbt.traverse();
	}
	
}

class RedBlackTree{
	TreeNode root;
	static final int RED =0;
	static final int BLACK = 1;
	private class TreeNode{
		int value;
		int color;
		TreeNode parentNode;
		TreeNode leftNode;
		TreeNode rightNode;
		public TreeNode(int value){
			this.value = value;
			this.parentNode = null;
			this.leftNode = null;
			this.rightNode = null;
		}
	}
	
	
	
	
	//左旋：将node及其左子树变为node右儿子的左子树
	public void rotateLeft(TreeNode node){
		if(node!=null){
			TreeNode parent = node.parentNode;
			TreeNode right = node.rightNode;
			if(right!=null){
				//将node的右子树连接到node的父节点上
				right.parentNode = parent;
				if(parent == null){
					root = right;
				}
				else if(parent.leftNode ==  node)
					parent.leftNode = right;
				else
					parent.rightNode = right;
				
				//将node的右孩子的左孩子赋为node的右孩子
				if(right.leftNode != null)
					right.leftNode.parentNode = node;
				node.rightNode = right.leftNode;
				//将node赋为node右孩子的左孩子
				node.parentNode = right;
				right.leftNode = node;
				
			}
		}
		
	}
	//右旋：将node及其右子树，变为node的左儿子的右子树
	public void rotateRight(TreeNode node){
		if(node!=null){
			TreeNode left =  node.leftNode;
			TreeNode parent = node.parentNode;
			if(left!=null){
				
				node.leftNode =left.rightNode;
				if(left.rightNode!=null)
					left.rightNode.parentNode = node;
				
				left.rightNode = node;
				node.parentNode = left;
				
				left.parentNode =parent;
				if(parent==null)
					root = left;
				else if(parent.leftNode == node)
					parent.leftNode = left;
				else
					parent.rightNode = left;
			}
		}
	}
	
	public TreeNode search(int value){
		TreeNode n = root;
		while(n!=null){
			if(value==n.value)
				return n;
			else if(value<n.value)
				n=n.leftNode;
			else
				n=n.rightNode;
		}
		return null;
	}
	//插入
	public void insert(int value){
		//二叉搜索树插入
		TreeNode node = new TreeNode(value);
		TreeNode current  = root;
		TreeNode parent = null;
		boolean isLeft = false;
		while(current!=null){
			parent = current;
			if(node.value < current.value){
				isLeft = true;
				current = current.leftNode;
			}
			else{
				isLeft = false;
				current = current.rightNode;
			}
		}
		if(parent == null)
			root = node;
		else{
			if(isLeft){
				parent.leftNode = node;
			}
			else{
				parent.rightNode = node;
			}
		}
		node.parentNode = parent;
		node.color = RED;
		rbInsertFixup(node);
	}
	//插入修正
	private  void rbInsertFixup(TreeNode node){
		while(node!=null && node!=root && node.parentNode.color == RED){
			TreeNode parent = node.parentNode;
			TreeNode gParent = parent.parentNode;
			//父节点是祖父节点的左孩子
			if(parent == gParent.leftNode){
				TreeNode uncle = gParent.rightNode;
				//case1:父节点为红色，叔叔节点为红色
				if(uncle!=null && uncle.color == RED ){
					parent.color = BLACK;
					gParent.color = RED;
					uncle.color = BLACK;
					node = gParent;
				}
				//uncle为null也是黑色
				else{
					//case2:父节点为红色，叔叔节点为黑色，当前节点是父节点的右孩子
					if(parent.rightNode ==  node){
						node = parent;
						rotateLeft(node);
					}
					//case3:父节点为红色，叔叔节点为黑色，当前节点是父节点的左孩子
					node.parentNode.color = BLACK;
					node.parentNode.parentNode.color = RED;
					rotateRight(node.parentNode.parentNode);
				}
				
			}
			//父节点是祖父节点的右孩子，调整方向与上面相反
			else{
				TreeNode uncle = gParent.leftNode;
				if(uncle!=null && uncle.color==RED){
					parent.color = BLACK;
					uncle.color = BLACK;
					gParent.color = RED;
					node = gParent;
				}
				else{
					if(parent.leftNode == node){
						node = parent;
						rotateRight(node);
					}
					node.parentNode.color = BLACK;
					node.parentNode.parentNode.color = RED;
					rotateLeft(node.parentNode.parentNode);
				}
			}
		}
		
		root.color = BLACK;
	}
	public TreeNode delete(int value){
		TreeNode node = search(value);
		if(node==null)
			return null;
		else
			deleteNode(node);
		return node;
	}
	public void deleteNode(TreeNode node){
		TreeNode current = node;
		//TreeNode parent = current.parentNode;
		//被删除节点的左右子树均不为空，将删除操作转移到后继节点上
		if(current.leftNode !=null && current.rightNode!=null){
			TreeNode successor = current.rightNode;
			while(successor.leftNode!=null)
				successor = successor.leftNode;
			current.value = successor.value;
			current = successor;
		}
		//现在，被删除节点（current）的左右子树至少有一个为空
		TreeNode replaceNode = current.leftNode != null ? current.leftNode : current.rightNode;
		if(current.parentNode!=null){
			if(current.parentNode.leftNode == current)
				current.parentNode.leftNode = replaceNode;
			else
				current.parentNode.rightNode = replaceNode;
		}
		else{
			root = replaceNode;
		}
		if(replaceNode!=null)
			replaceNode.parentNode =current.parentNode;
		if(current.color == BLACK){
			if(replaceNode == null)
				//此时的curren与树的连接只剩下current.parent
				deleteFixup(current);
			else
				deleteFixup(replaceNode);
		}
		current.leftNode = null;
		current.rightNode = null;
		current.parentNode =null;
//		//左右子树均为空
//		if(replaceNode==null){
//			if(current.parentNode!=null){
//				if(current.parentNode.leftNode == current)
//					current.parentNode.leftNode = null;
//				else
//					current.parentNode.rightNode =null;
//				current.parentNode = null;
//			}
//			else
//				root = null;
//		}
//		else{
//			if(current.parentNode!=null){
//				if(current.parentNode.leftNode == current){
//					current.parentNode.leftNode = replaceNode;
//				}
//				else{
//					current.parentNode.rightNode = replaceNode;
//				}
//				replaceNode.parentNode = current.parentNode;
//			}
//			else
//				root = replaceNode;
//		}
	}
	private void deleteFixup(TreeNode node){
		TreeNode parent = node.parentNode;
		//此情况是node为即将被删除的节点，且树中只剩该节点，在前面的逻辑下，此时root已经为null
		if(parent == null){
			//node不可能为null
			node.color = BLACK;
			return;
		}
		while(node!=root && getColor(node) == BLACK){
			//parent为空的情况下，node必为root.
			parent = node.parentNode;
			if(node == parent.leftNode){
				TreeNode sib = parent.rightNode;
				//sib可能为空，因此使用getColor等处理它
				if( getColor(sib) == RED){
					parent.color = RED;
					setColor(sib,BLACK);
					rotateLeft(parent);
					sib = parent.rightNode;
				}
				
				if(getColor(getLeft(sib))==BLACK && getColor(getRight(sib))==BLACK){
					setColor(sib,RED);
					node = parent;
				}
				else{
					if(getColor(getRight(sib)) == BLACK){
						setColor(getLeft(sib), BLACK);
                        setColor(sib, RED);
                        rotateRight(sib);
                        sib = getRight(parent);
					}
					setColor(sib, getColor(parent));
                    parent.color = BLACK;
                    setColor(getRight(sib), BLACK);
                    rotateLeft(parent);
                    node = root;
				}
			}
			else{
				
				TreeNode sib = parent.leftNode;
				if (getColor(sib) == RED) {
                    setColor(sib, BLACK);
                    parent.color = RED;
                    rotateRight(parent);
                    sib = parent.leftNode;
                }

                if (getColor(getRight(sib)) == BLACK &&getColor(getLeft(sib)) == BLACK) {
                    setColor(sib, RED);
                    node = parent;
                } 
                else {
                	
                    if (getColor(getLeft(sib)) == BLACK) {
                        setColor(getRight(sib), BLACK);
                        setColor(sib, RED);
                        rotateLeft(sib);
                        sib = parent.leftNode;
                    }
                    setColor(sib, getColor(parent));
                    parent.color =  BLACK;
                    setColor(getLeft(sib), BLACK);
                    rotateRight(parent);
                    node = root;
                }
			}
		}
		setColor(node,BLACK);
	}
	//中序遍历
	public void traverse(){
		traverseHelper(root);
	}
	private void traverseHelper(TreeNode node){
		if(node!=null){
			traverseHelper(node.leftNode);
			System.out.print(node.value+" ");
			traverseHelper(node.rightNode);
		}
	}
	
	//打印树结构
	public void printTreeStruct(){
		ArrayList<ArrayList<TreeNode>> list = new ArrayList();
		//ArrayList<Integer> list = new ArrayList<Integer>();
		TreeStructHelper(root,0,list);
		System.out.print("\n");
		for(ArrayList<TreeNode> subList:list){
			Iterator<TreeNode> it = subList.iterator();
			while(it.hasNext()){
				TreeNode ele = it.next();
				if(ele!=null)
					System.out.print(ele.value+"("+(ele.color==RED?"R":"B")+
							" "+(ele.parentNode == null ? "root":ele.parentNode.value)
							+")"+"  ");
				else
					System.out.print("NULL"+"  ");
			}
			System.out.print("\n");
		}
	}
	//宽度优先遍历
	private void TreeStructHelper(TreeNode node,int depth,ArrayList<ArrayList<TreeNode>> list){
		if(list.size()<=depth){
			list.add(new ArrayList<TreeNode>());
		}
		if(node==null){
			list.get(depth).add(null);
		}
		else{
			list.get(depth).add(node);
			TreeStructHelper(node.leftNode,depth+1,list);
			TreeStructHelper(node.rightNode,depth+1,list);
		}
	}
	//这些函数用来处理删除调整过程中节点为null的情况
	private int getColor(TreeNode node){
		return node == null?BLACK:node.color;
	}
	private void setColor(TreeNode node,int c){
		if(node!=null)
			node.color = c;
	}
	private TreeNode getLeft(TreeNode node){
		return node == null?null:node.leftNode;
	}
	private TreeNode getRight(TreeNode node){
		return node == null ? null:node.rightNode;
	}
}
