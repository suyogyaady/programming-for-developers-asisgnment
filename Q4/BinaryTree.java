package Q4;

class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;

    TreeNode(int val) {
        this.val = val;
    }
}

public class BinaryTree {
    private TreeNode[] findNodeAndParent(TreeNode node, int target, TreeNode parent, int depth) {
        if (node == null) {
            return null;
        }

        if (node.val == target) {
            return new TreeNode[] { node, parent, new TreeNode(depth) };
        }

        TreeNode[] leftResult = findNodeAndParent(node.left, target, node, depth + 1);
        if (leftResult[0] != null) {
            return leftResult;
        }

        TreeNode[] rightResult = findNodeAndParent(node.right, target, node, depth + 1);
        if (rightResult[0] != null) {
            return rightResult;
        }

        return new TreeNode[] { null, null, new TreeNode(-1) };
    }

    public boolean areBrothers(TreeNode root, int x, int y) {
        TreeNode[] xResult = findNodeAndParent(root, x, null, 0);
        TreeNode[] yResult = findNodeAndParent(root, y, null, 0);

        return xResult[2].val == yResult[2].val && xResult[1] != yResult[1];
    }

    // Test case
    public static void main(String[] args) {
        BinaryTree tree = new BinaryTree();

        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.left = new TreeNode(4);

        int x = 4;
        int y = 3;

        System.out.println(tree.areBrothers(root, x, y)); // Output: false
    }
}
