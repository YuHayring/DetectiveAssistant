package cn.hayring.caseanalyst.view.magic;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import cn.hayring.caseanalyst.R;
import cn.hayring.caseanalyst.domain.Case;
import cn.hayring.caseanalyst.domain.Organization;
import cn.hayring.caseanalyst.domain.Person;
import cn.hayring.caseanalyst.domain.Relationship;
import cn.hayring.caseanalyst.utils.Pointer;
import cn.hayring.cytoscape.CytoscapeView;
import cn.hayring.cytoscape.bean.BaseElement;
import cn.hayring.cytoscape.bean.Edge;
import cn.hayring.cytoscape.bean.Node;

/**
 * @author Hayring
 * @date 2021/8/23
 * @description
 */

public class PersonGraphV3 extends AppCompatActivity {


    /***
     * 二维滑动ScrollView
     */
    protected CytoscapeView cytoscapeView;

    /***
     * 案件实例
     */
    protected Case caseInstance;

    /***
     * Person在ViewGroup中的view-id
     */
    protected BiMap<Person, Integer> idSet = HashBiMap.create();
    protected BiMap<Integer, Person> personSet = idSet.inverse();


    /***
     * 生命周期加载方法
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_graph_v3);
        caseInstance = (Case) Pointer.getPoint();
        cytoscapeView = findViewById(R.id.cytoscape);
    }


    /***
     * view加载完之后画连接线操作
     * @param hasFocus
     */
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        List<BaseElement> elements = new ArrayList<>();
        Long autoincrease = 1L;
        //绘制无组织人关系图
        for (Person a : caseInstance.getNonOrgPersons()) {
            a.setId(autoincrease++);
            Node node = new Node();
            Map<String, Object> data = new HashMap<>();
            data.put("id", String.valueOf(a.getId()));
            data.put("name", a.getName());
            node.setData(data);
            elements.add(node);
        }

        //按组织绘制关系图
        for (Organization org : caseInstance.getOrganizations()) {

            //按人绘制图
            for (Person a : org.getMembers()) {
                a.setId(autoincrease++);
                Node node = new Node();
                Map<String, Object> data = new HashMap<>();
                data.put("id", String.valueOf(a.getId()));
                data.put("name", a.getName());
                node.setData(data);
                elements.add(node);

            }
        }
        GraphLoopTest tool = new GraphLoopTest(caseInstance.getPersons());
        //广度优先画图
        tool.BFSSearch(caseInstance.getPersons().get(0), elements);

        cytoscapeView.add(elements);
    }


    /**
     * 使用java实现图的图的广度优先 和深度优先遍历算法，遍历Person和Relationship画关系图
     */
    static class GraphLoopTest {
        private HashMap<Person, ArrayList<Relationship<Person, Person>>> graph = new HashMap<Person, ArrayList<Relationship<Person, Person>>>();
        private HashMap<Relationship<Person, Person>, Boolean> visited = new HashMap<Relationship<Person, Person>, Boolean>();
        private Long autoincrease = 1L;
        private List<BaseElement> elements;

        /**
         * 初始化图数据：使用邻居表来表示图数据。
         */
        public GraphLoopTest(ArrayList<Person> persons) {
//        图结构如下
//          1
//        /   \
//       2     3
//      / \   / \
//     4  5  6  7
//      \ | / \ /
//        8    9
            /*graph.put("1", Arrays.asList("2", "3"));
            graph.put("2", Arrays.asList("1", "4", "5"));
            graph.put("3", Arrays.asList("1", "6", "7"));
            graph.put("4", Arrays.asList("2", "8"));
            graph.put("5", Arrays.asList("2", "8"));
            graph.put("6", Arrays.asList("3", "8", "9"));
            graph.put("7", Arrays.asList("3", "9"));
            graph.put("8", Arrays.asList("4", "5", "6"));
            graph.put("9", Arrays.asList("6", "7"));*/

            for (Person a : persons) {
                graph.put(a, a.getManManRelationships());
                for (Relationship<Person, Person> re : a.getManManRelationships()) {
                    visited.put(re, false);
                }
            }
        }

        /**
         * 宽度优先搜索(BFS, Breadth First Search)
         * BFS使用队列(queue)来实施算法过程
         */
        private Queue<Person> queue = new LinkedList<Person>();
        private Map<Person, Boolean> status = new HashMap<Person, Boolean>();

        /**
         * 开始点
         *
         * @param person
         */
        public void BFSSearch(Person person, List<BaseElement> elements) {
            //1.把起始点放入queue；
            queue.add(person);
            status.put(person, false);
            this.elements = elements;
            bfsLoop();
        }

        private void bfsLoop() {
            //  1) 从queue中取出队列头的点；更新状态为已经遍历。
            Person currentQueueHeader = queue.poll(); //出队

            //访问节点
            List<Relationship<Person, Person>> neighborPoints = graph.get(currentQueueHeader);
            for (Relationship<Person, Person> re : neighborPoints) {
                if (!visited.get(re)) {
                    Person nextPerson = re.getItemE().equals(currentQueueHeader) ? re.getItemT() : re.getItemE();

                    Edge edge = new Edge();
                    Map<String, Object> data = new HashMap<>();
                    data.put("id", "e" + String.valueOf(autoincrease++));
                    data.put("source", nextPerson.getId().toString());
                    data.put("target", currentQueueHeader.getId().toString());
                    edge.setData(data);
                    elements.add(edge);


                    visited.put(re, true);
                }
            }
            //访问完成
            status.put(currentQueueHeader, true);

            //System.out.println(currentQueueHeader);
            //  2) 找出与此点邻接的且尚未遍历的点，进行标记，然后全部放入queue中。

            for (Relationship<Person, Person> poinit : neighborPoints) {
                Person nextPerson = poinit.getItemE().equals(currentQueueHeader) ? poinit.getItemT() : poinit.getItemE();
                if (!status.getOrDefault(nextPerson, false)) { //未被遍历
                    if (queue.contains(nextPerson)) continue;
                    queue.add(nextPerson);
                    status.put(nextPerson, false);
                }
            }
            if (!queue.isEmpty()) {  //如果队列不为空继续遍历
                bfsLoop();
            }
        }


        /**
         * 深度优先搜索(DFS, Depth First Search)
         * DFS使用队列(queue)来实施算法过程
         * stack具有后进先出LIFO(Last Input First Output)的特性，DFS的操作步骤如下：
         */
//     1、把起始点放入stack；
//     2、重复下述3步骤，直到stack为空为止：
//    从stack中访问栈顶的点；
//    找出与此点邻接的且尚未遍历的点，进行标记，然后全部放入stack中；
//    如果此点没有尚未遍历的邻接点，则将此点从stack中弹出。
/*
        private Stack<Person> stack = new Stack<Person>();
        public void DFSSearch(Person startPoint) {
            stack.push(startPoint);
            status.put(startPoint, true);
            dfsLoop();
        }

        private void dfsLoop() {
            if(stack.empty()){
                return;
            }
            //查看栈顶元素，但并不出栈
            Person stackTopPoint = stack.peek();
            //  2) 找出与此点邻接的且尚未遍历的点，进行标记，然后全部放入queue中。
            List<Relationship<Person,Person>> neighborPoints = graph.get(stackTopPoint);
            for (Relationship<Person,Person> re : neighborPoints) {
                Person nextPerson = re.getItemE().equals(stackTopPoint)?re.getItemT():re.getItemE();
                if (!status.getOrDefault(nextPerson, false)) { //未被遍历
                    stack.push(nextPerson);
                    status.put(nextPerson, true);
                    dfsLoop();
                }
            }
            Person popPoint =  stack.pop();
            //System.out.println(popPoint);
        }*/


    }


}

