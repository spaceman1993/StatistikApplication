package widgets.table;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import hilfklassen.DBHandler;
import hilfklassen.Kategorie;
import hilfklassen.Spieler;
import hilfklassen.Statistik;
import hilfklassen.Statistikwerte;
import szut.de.statistikapplication.R;

public class Table extends RelativeLayout {

    public final String TAG = "TableMainLayout.java";

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Activity activity;
    Context context;
    Statistik statistik;

    ArrayList<View> headerObjects;
    ArrayList<DataObject> dataObjects;

    ArrayList<Spieler> spieler;
    ArrayList<ArrayList<Statistikwerte>> spielerstatistikwerte;
    ArrayList<Kategorie> kategorien;

    ArrayList<Integer> headerCellsWidth = new ArrayList<Integer>();

    public Table(Activity activity, Statistik statistik) {

        super(activity.getApplicationContext());

        this.activity = activity;
        this.context = activity.getApplicationContext();
        this.statistik = statistik;

        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        this.initVariables();
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();


        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_D();

        this.resizeBodyTableRowHeight();
    }

    // this is just the sample data
    ArrayList<DataObject> dataObjects(Activity activity, ArrayList<Spieler> spieler, ArrayList<ArrayList<Statistikwerte>> statistikwerte){

        ArrayList<DataObject> dataObjects = new ArrayList<DataObject>();

        for (int i=0; i<spieler.size(); i++){
            DataObject dataObject = new DataObject(activity, spieler.get(i), statistikwerte.get(i));
            dataObjects.add(dataObject);
        }


        return dataObjects;

    }

    private void initVariables(){
        DBHandler dbHandler = new DBHandler(context, null, null, 1);

        kategorien = dbHandler.getKategorienDerStatistik(statistik);
        headerObjects = new HeaderObjects(activity, kategorien).getHeaderViews();

        spieler = dbHandler.getSpielerDerStatistik(statistik);
        spielerstatistikwerte = new ArrayList<ArrayList<Statistikwerte>>();

        for(int i=0; i<spieler.size(); i++){
            spielerstatistikwerte.add(dbHandler.getStatistikwerteDerStatistikDesSpielers(statistik, spieler.get(i)));
        }

        dataObjects = dataObjects(activity, spieler, spielerstatistikwerte);

        for(int i=0; i<headerObjects.size(); i++){
            headerCellsWidth.add(headerObjects.get(i).getWidth());
        }

        dbHandler.close();
    }

    // initalized components
    private void initComponents(){

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);
    }

    // set essential component IDs
    private void setComponentsId(){
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag(){

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout(){

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        RelativeLayout.LayoutParams componentB_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        RelativeLayout.LayoutParams componentC_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        RelativeLayout.LayoutParams componentD_Params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }



    private void addTableRowToTableA(){
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB(){
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow(){

        TableRow componentATableRow = new TableRow(this.context);
        View view = this.headerObjects.get(0);
        componentATableRow.addView(view);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow(){

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headerObjects.size();

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
//        params.setMargins(-1, 0, 0, 0);

        for(int x=0; x<(headerFieldCount-1); x++){
            View view = headerObjects.get(x+1);

            componentBTableRow.addView(view, params);
        }


        return componentBTableRow;
    }

    // generate table row of table C and table D
    private void generateTableC_AndTable_D(){

        // just seeing some header cell width
        for(int x=0; x<this.headerCellsWidth.size(); x++){
            Log.v("TableMainLayout.java", this.headerCellsWidth.get(x)+"");
        }

        for(DataObject dataObject : this.dataObjects){

            TableRow tableRowForTableC = this.tableRowForTableC(dataObject);
            TableRow tableRowForTableD = this.taleRowForTableD(dataObject);

            this.tableC.addView(tableRowForTableC);
            this.tableD.addView(tableRowForTableD);

        }
    }

    // a TableRow for table C
    TableRow tableRowForTableC(DataObject dataObject){

        TableRow.LayoutParams params = new TableRow.LayoutParams( this.headerCellsWidth.get(0),LayoutParams.MATCH_PARENT);
//        params.setMargins(0, -1, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);

        View view = dataObject.getDataviews().get(0);

        tableRowForTableC.addView(view,params);

        return tableRowForTableC;
    }

    TableRow taleRowForTableD(DataObject dataObject){

        TableRow taleRowForTableD = new TableRow(this.context);

        int loopCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();
        ArrayList<View> info = new ArrayList<View>();

        for(int i=1; i<dataObject.getDataviews().size(); i++){
            info.add(dataObject.getDataviews().get(i));
        }

        for(int x=0 ; x<loopCount; x++){
            TableRow.LayoutParams params = new TableRow.LayoutParams( headerCellsWidth.get(x+1),LayoutParams.MATCH_PARENT);
            //params.setMargins(2, 2, 0, 0);

            View viewB = info.get(x);

            taleRowForTableD.addView(viewB,params);
        }

        return taleRowForTableD;

    }

    // table cell standard TextView
    TextView bodyTextView(String label){

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
//        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // header standard TextView
    TextView headerTextView(String label){

        TextView headerTextView = new TextView(this.context);
        headerTextView.setText(label);
        headerTextView.setGravity(Gravity.CENTER);
        //headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow)  this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth(){

        int tableAChildCount = ((TableRow)this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow)this.tableB.getChildAt(0)).getChildCount();

        for(int x=0; x<(tableAChildCount+tableBChildCount); x++){

            if(x==0){
                this.headerCellsWidth.set(x, this.viewWidth(((TableRow)this.tableA.getChildAt(0)).getChildAt(x)));
            }else{
                this.headerCellsWidth.set(x, this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1)));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight(){

        int tableC_ChildCount = this.tableC.getChildCount();

        for(int x=0; x<tableC_ChildCount; x++){

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow)  this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if(tableRow.getChildCount()==1){

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return ;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView{

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("horizontal scroll view b")){
                horizontalScrollViewD.scrollTo(l, 0);
            }else{
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView{

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if(tag.equalsIgnoreCase("scroll view c")){
                scrollViewD.scrollTo(0, t);
            }else{
                scrollViewC.scrollTo(0,t);
            }
        }
    }


}