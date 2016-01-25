package com.tpadsz.utils.merger.test;

import com.tpadsz.utils.merger.entities.PathBean;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

/**
 * Created by roger.wang on 2016/1/25.
 */
public class TestPathBeans {
    String[] paths=null;

    @Before
    public void setUp(){
        paths=new String[]{"ctc","ctc_cic","ctc_cic_uic"};
    }

    @Test
    public void testNewPathsFromStrings(){
        PathBean ptr=PathBean.newPathBeansChainFromStrings(paths);
        for(int i=0;i<paths.length;i++){
            assert ptr.getKey().equals(paths[i]);
            ptr=ptr.getSubPath();
        }

        assert ptr==null;

    }


    @Test
    public void testGetPathLength(){
        String[] paths1=this.paths;
        assert PathBean.newPathBeansChainFromStrings(paths1).getPathLength()==3;

        String[] paths2=new String[]{"cic"};
        assert PathBean.newPathBeansChainFromStrings(paths2).getPathLength()==1;

    }

    @Test
    public void testGetSubPathStringsAtIndex(){
        PathBean root=PathBean.newPathBeansChainFromStrings(paths);
        assert root.getSubPathStringAtIndex(0).length==3;
        assert root.getSubPathStringAtIndex(1).length==2;

    }

    @Test
    public void testToPathStrings(){
        PathBean root=PathBean.newPathBeansChainFromStrings(paths);

        String[] pathsCalculated=root.toPathStings();

        assert paths.length==pathsCalculated.length;
        for(Integer i=0;i<paths.length;i++){
            System.out.println(String.format("Path[%d]: %s\nPathCalculated[%d]: %s\n\n",i,paths[i],i,pathsCalculated[i]));
            assert paths[i].equals(pathsCalculated[i]);
        }
    }

    @Test
    public void testNewPathsFromIdKey(){
        String _id="ctc_cic_uic";
        PathBean root=PathBean.newPathBeansChainFromIdKey(_id);
        assert root.getPathLength()==3;

        assert root.getKey().equals("ctc");
        assert root.getSubPath().getKey().equals("ctc_cic");
        assert root.getSubPath().getSubPath().getKey().equals("ctc_cic_uic");
    }

    @Test
    public void testFindMaxIndexOfSamePaths(){
        // For two identical paths;
        String[] paths1=this.paths;
        String[] paths2= Arrays.copyOf(this.paths,this.paths.length);

        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==2;

        // For two totally different paths.;
        paths1=this.paths;
        paths2=new String[]{"x","x_y"};
        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==-1;
        paths2=new String[]{"x","x_y","x_y_z"};
        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==-1;
        paths2=new String[]{"x","x_y","x_y_z","x_y_z_a"};
        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==-1;

        // For partially identical paths;
        paths1=this.paths;
        paths2=new String[]{"ctc","ctc_cic","ctc_cic_xx"};
        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==1;
        paths2=new String[]{"ctc","ctc_xxx","ctc_cic_xx"};
        assert PathBean.newPathBeansChainFromStrings(paths1).findMaxIndexOfSamePaths(paths2)==0;
    }

    @Test
    public void testGetSubPathAtIndex() {
        PathBean root = PathBean.newPathBeansChainFromStrings(this.paths);

        assert root.getSubPathBeanAtIndex(0).getKey().equals("ctc");
        assert root.getSubPathBeanAtIndex(1).getKey().equals("ctc_cic");
        assert root.getSubPathBeanAtIndex(2).getKey().equals("ctc_cic_uic");
    }

    @Test
    public void testSubstitutePathsWithBranch(){
        PathBean root=PathBean.newPathBeansChainFromStrings(this.paths);
        PathBean branch=PathBean.newPathBeansChainFromIdKey("a_b_c");

        PathBean[] ret=root.substitutePaths(branch, 1);
        PathBean newPath=ret[0];
        PathBean substituted=ret[1];

        assert newPath!=null;
        assert substituted!=null;
//        String[] strings=null;
//        strings=newPath.toPathStings();
//        System.err.println("-------------------------");
//        for(String str:strings){
//            System.err.println(str);
//        }
//        System.err.println("-------------------------");
//        strings=substituted.toPathStings();
//        for(String str:strings){
//            System.err.println(str);
//        }
        assert newPath.getPathLength()==5;
        assert substituted.getPathLength()==1;
        assert substituted.getKey().equals("ctc_cic");

    }

}
