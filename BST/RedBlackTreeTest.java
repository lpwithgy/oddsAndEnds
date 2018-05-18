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
	
	
	
	
	//��������node������������Ϊnode�Ҷ��ӵ�������
	public void rotateLeft(TreeNode node){
		if(node!=null){
			TreeNode parent = node.parentNode;
			TreeNode right = node.rightNode;
			if(right!=null){
				//��node�����������ӵ�node�ĸ��ڵ���
				right.parentNode = parent;
				if(parent == null){
					root = right;
				}
				else if(parent.leftNode ==  node)
					parent.leftNode = right;
				else
					parent.rightNode = right;
				
				//��node���Һ��ӵ����Ӹ�Ϊnode���Һ���
				if(right.leftNode != null)
					right.leftNode.parentNode = node;
				node.rightNode = right.leftNode;
				//��node��Ϊnode�Һ��ӵ�����
				node.parentNode = right;
				right.leftNode = node;
				
			}
		}
		
	}
	//��������node��������������Ϊnode������ӵ�������
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
	//����
	public void insert(int value){
		//��������������
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
	//��������
	private  void rbInsertFixup(TreeNode node){
		while(node!=null && node!=root && node.parentNode.color == RED){
			TreeNode parent = node.parentNode;
			TreeNode gParent = parent.parentNode;
			//���ڵ����游�ڵ������
			if(parent == gParent.leftNode){
				TreeNode uncle = gParent.rightNode;
				//case1:���ڵ�Ϊ��ɫ������ڵ�Ϊ��ɫ
				if(uncle!=null && uncle.color == RED ){
					parent.color = BLACK;
					gParent.color = RED;
					uncle.color = BLACK;
					node = gParent;
				}
				//uncleΪnullҲ�Ǻ�ɫ
				else{
					//case2:���ڵ�Ϊ��ɫ������ڵ�Ϊ��ɫ����ǰ�ڵ��Ǹ��ڵ���Һ���
					if(parent.rightNode ==  node){
						node = parent;
						rotateLeft(node);
					}
					//case3:���ڵ�Ϊ��ɫ������ڵ�Ϊ��ɫ����ǰ�ڵ��Ǹ��ڵ������
					node.parentNode.color = BLACK;
					node.parentNode.parentNode.color = RED;
					rotateRight(node.parentNode.parentNode);
				}
				
			}
			//���ڵ����游�ڵ���Һ��ӣ����������������෴
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
		//��ɾ���ڵ��������������Ϊ�գ���ɾ������ת�Ƶ���̽ڵ���
		if(current.leftNode !=null && current.rightNode!=null){
			TreeNode successor = current.rightNode;
			while(successor.leftNode!=null)
				successor = successor.leftNode;
			current.value = successor.value;
			current = successor;
		}
		//���ڣ���ɾ���ڵ㣨current������������������һ��Ϊ��
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
				//��ʱ��curren����������ֻʣ��current.parent
				deleteFixup(current);
			else
				deleteFixup(replaceNode);
		}
		current.leftNode = null;
		current.rightNode = null;
		current.parentNode =null;
//		//����������Ϊ��
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
		//�������nodeΪ������ɾ���Ľڵ㣬������ֻʣ�ýڵ㣬��ǰ����߼��£���ʱroot�Ѿ�Ϊnull
		if(parent == null){
			//node������Ϊnull
			node.color = BLACK;
			return;
		}
		while(node!=root && getColor(node) == BLACK){
			//parentΪ�յ�����£�node��Ϊroot.
			parent = node.parentNode;
			if(node == parent.leftNode){
				TreeNode sib = parent.rightNode;
				//sib����Ϊ�գ����ʹ��getColor�ȴ�����
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
	//�������
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
	
	//��ӡ���ṹ
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
	//������ȱ���
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
	//��Щ������������ɾ�����������нڵ�Ϊnull�����
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
