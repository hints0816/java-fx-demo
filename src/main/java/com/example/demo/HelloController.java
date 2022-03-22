package com.example.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.utils.HttpUtils;
import com.example.demo.utils.StringUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.util.Callback;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    private TableView<Person> personTable;

    @FXML
    private TableView<Order> orderTableView;

    @FXML
    private TableColumn<Person, String> firstNameColumn;
    @FXML
    private TableColumn<Person, String> lastNameColumn;

    @FXML
    private TableColumn<Order, String> prid;
    @FXML
    private TableColumn<Order, String> prde;

    @FXML
    private TextField usid;

    @FXML
    private TextField pridp;

    @FXML
    private TextField usidp;

    @FXML
    private DatePicker start;

    @FXML
    private DatePicker end;

    @FXML
    private Pagination pagination;


   private SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public HelloController() {
    }

    @FXML
    private void initialize() {
        // Initialize the person table with the two columns.
        prid.setCellValueFactory(cellData -> cellData.getValue().pridProperty());
        prde.setCellValueFactory(cellData -> cellData.getValue().prdeProperty());

        // Initialize the person table with the two columns.
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        //页属性出现变化触发
        pagination.setPageFactory(new Callback<Integer, Node>() {
            @Override
            public Node call(Integer param) {
                if(param <= -1) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Warning Dialog");
                    alert.setHeaderText("Look, a Warning Dialog");
                    alert.setContentText("没有前一页了");
                    alert.showAndWait();
                }else{
                    HashMap map = new HashMap<String,Object>();
                    if(StringUtils.isNotEmpty(pridp.getText())){
                        map.put("prid",pridp.getText());
                    }
                    if(StringUtils.isNotEmpty(usidp.getText())){
                        map.put("pnam",usidp.getText());
                    }
                    if(start.getValue()!=null){
                        map.put("start",start.getValue().toString());
                    }
                    if(end.getValue()!=null){
                        map.put("end",end.getValue().toString());
                    }
                    map.put("pageNum",String.valueOf(param+1));
                    map.put("pageSize","10");
                    loadOrder(map);
                }
                return new Label();
            }
        });

        showPersonDetails(null);
        personObservableList.clear();

        // Listen for selection changes and show the person details when changed.
        orderTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue));


    }

    private void showPersonDetails(Order order) {
        personObservableList.clear();
        if (order != null) {
            /*String string = HttpUtils.sendGet("http://gate.scmaction.gree.com/gscmservice/loginlog/getorder","pageNum=1&pageSize=1&prid="+order.getPrid());
            JSONObject formDescJson = (JSONObject) JSON.parse(string);
            JSONArray jsonArray = formDescJson.getJSONArray("dataList");
            JSONObject jsonObject = jsonArray.getJSONObject(0);*/

            for(Object object:list){
                JSONObject jsonObject = (JSONObject) object;
                if(jsonObject.getString("prid").equals(order.getPrid())){
                    personObservableList.add(new Person("单号",jsonObject.getString("prid")));
                    personObservableList.add(new Person("申请类型",jsonObject.getString("prde")));
                    personObservableList.add(new Person("主题",jsonObject.getString("them")));
                    personObservableList.add(new Person("申请人",jsonObject.getString("nama")));
                    String sd = sdf.format(new Date(Long.parseLong(String.valueOf(jsonObject.getString("pdat")))));

                    personObservableList.add(new Person("申请时间",sd));
                    personObservableList.add(new Person("当前节点",jsonObject.getString("dsca")));
                    personObservableList.add(new Person("备注1",jsonObject.getString("rmk1")));
                    personObservableList.add(new Person("备注2",jsonObject.getString("rmk1")));
                    personTable.setItems(personObservableList);
                }
            }
        }
    }

    private ObservableList<Person> personObservableList = FXCollections.observableArrayList();
    private List list = new ArrayList();

    private Integer pageNumber;

    private Integer recordCount;

    public void setMainApp() {

        // Add observable list data to the table
        HashMap map = new HashMap<String,Object>();
        map.put("pageNum","1");
        map.put("pageSize","10");
        loadOrder(map);
    }

    public void loadOrder(HashMap<String,Object> map) {
        list.clear();
        ObservableList<Order> orderObservableList = FXCollections.observableArrayList();
        String para = "";
        for(String key:map.keySet()){
            para+=key+"="+map.get(key).toString()+"&";
        }
        para = para.substring(0,para.length()-1);
        String string = HttpUtils.sendGet("http://gate.scmaction.gree.com/gscmservice/loginlog/getorder",para);

        JSONObject formDescJson = (JSONObject) JSON.parse(string);
        JSONArray jsonArray = formDescJson.getJSONArray("dataList");
        JSONObject jsonObject = formDescJson.getJSONObject("page");
        pageNumber = jsonObject.getInteger("pageNumber")-1;

        if(recordCount != jsonObject.getInteger("recordCount")){
            recordCount = jsonObject.getInteger("recordCount");
            pagination.setPageCount(recordCount);
        }
        for(Object jsonObject1:jsonArray) {
            JSONObject jsonObject2 = (JSONObject)jsonObject1;
            orderObservableList.add(new Order(jsonObject2.getString("prid"), jsonObject2.getString("pnam")));
            list.add(jsonObject2);
        }
        orderTableView.setItems(orderObservableList);
    }

    @FXML
    protected void onHelloButtonClick() throws IOException {
        HashMap map = new HashMap<String,Object>();
        if(StringUtils.isNotEmpty(pridp.getText())){
            map.put("prid",pridp.getText());
        }
        if(StringUtils.isNotEmpty(usidp.getText())){
            map.put("pnam",usidp.getText());
        }
        if(start.getValue()!=null){
            map.put("start",start.getValue().toString());
        }
        if(end.getValue()!=null){
            map.put("end",end.getValue().toString());
        }
        map.put("pageNum","1");
        map.put("pageSize","10");
        loadOrder(map);
    }

    @FXML
    protected void onHelloButtonClick1(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info Dialog");
        alert.setHeaderText("提醒");
        alert.setContentText("提交成功");
        alert.showAndWait();
    }

}