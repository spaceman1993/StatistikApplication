package widgets.swipemenu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;

import database.DBHandler;
import database.data.Kategorie;
import database.data.Mannschaft;
import database.data.SelectableItem;
import database.data.Spieler;
import database.data.Statistik;
import hilfklassen.listview.CustomArrayAdapter;
import hilfklassen.sorter.SortName;
import hilfklassen.sorter.SortTrikonummer;
import szut.de.statistikapplication.R;
import szut.de.statistikapplication.createMannschaftActivities.NewKategorieActivity;
import szut.de.statistikapplication.createMannschaftActivities.NewSpielerActivity;
import szut.de.statistikapplication.createStatiActivities.ErfasseNeueStatiActivity;
import szut.de.statistikapplication.showStatiActivities.ErgebnistabelleActivity;
import widgets.swipemenulistview.SwipeMenu;
import widgets.swipemenulistview.SwipeMenuCreator;
import widgets.swipemenulistview.SwipeMenuItem;
import widgets.swipemenulistview.SwipeMenuListView;

/**
 * Created by roese on 20.02.2015.
 */
public class SwipeMenuEditDelete <T extends SelectableItem>{

    Mannschaft mannschaft;
    SwipeMenuListView swipeMenuListView;

    Activity activity;
    ArrayList<T> selectorList;
    ArrayAdapter<T> adapter;
    int pos;
    T selectableKlasse;
    int listviewItemId;
    boolean isMenuItemsShowing;
    boolean isDatabaseUpdating;
    boolean isUnactivItemsShowing;
    boolean isStatusChanging;
    boolean isDataNull = true;

    public SwipeMenuEditDelete(Activity activity, Mannschaft mannschaft, SwipeMenuListView swipeMenuListView, T selectableKlasse, int listviewItemId, boolean isMenuItemsShowing, boolean isDatabaseUpdating, boolean isUnactivItemsShowing, boolean isStatusChanging){

        this.activity = activity;
        this.mannschaft = mannschaft;
        this.swipeMenuListView = swipeMenuListView;
        this.selectableKlasse = selectableKlasse;
        this.listviewItemId = listviewItemId;
        this.isMenuItemsShowing = isMenuItemsShowing;
        this.isDatabaseUpdating = isDatabaseUpdating;
        this.isUnactivItemsShowing = isUnactivItemsShowing;
        this.isStatusChanging = isStatusChanging;

        if(isMenuItemsShowing) {
            SwipeMenuCreator creator = new SwipeMenuCreator() {
                @Override
                public void create(SwipeMenu menu) {
                    //create an action that will be showed on swiping an item in the list
                    SwipeMenuItem item1 = new SwipeMenuItem(getActivity().getApplicationContext());
                    item1.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                    item1.setWidth(125);
                    item1.setIcon(R.drawable.ic_action_about);
                    menu.addMenuItem(item1);

                    SwipeMenuItem item2 = new SwipeMenuItem(getActivity().getApplicationContext());
                    item2.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                    item2.setWidth(125);
                    item2.setIcon(R.drawable.ic_action_discard);
                    menu.addMenuItem(item2);

                    SwipeMenuItem item3 = new SwipeMenuItem(getActivity().getApplicationContext());
                    item3.setWidth(60);
                    menu.addMenuItem(item3);
                }
            };
            swipeMenuListView.setMenuCreator(creator);
        }
        swipeMenuListView.setOnItemClickListener(onItemClickListener);
        swipeMenuListView.setOnMenuItemClickListener(onMenuClickListener);

        updateListView();
    }

    public void setItemClickListener(AdapterView.OnItemClickListener listener){
        swipeMenuListView.setOnItemClickListener(listener);
    }

    public void setMenuItemClickListener(SwipeMenuListView.OnMenuItemClickListener listener){
        swipeMenuListView.setOnMenuItemClickListener(listener);
    }

    public void updateListView(){

        int index = swipeMenuListView.getFirstVisiblePosition();
        View v = swipeMenuListView.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - swipeMenuListView.getPaddingTop());

        if(isDatabaseUpdating || isDataNull) {
            selectorList = getListe(selectableKlasse);
            isDataNull = false;
        }
        selectorList = sortListe(selectorList);

        if(selectorList.size() == 0){
            selectorList.add((T) new Kategorie());
            adapter = new CustomArrayAdapter(activity, R.layout.swipemenu_item_leer, selectorList);
            isDataNull = true;
        }
        else{
            adapter = new CustomArrayAdapter(activity, listviewItemId, selectorList);
        }
        swipeMenuListView.setAdapter(adapter);
        swipeMenuListView.setSelectionFromTop(index, top);
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // TODO Auto-generated method stub

            if(isStatusChanging) {
                if(selectableKlasse instanceof Statistik){
                    Intent intent = new Intent(activity, ErgebnistabelleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("Statistik", (Statistik)selectorList.get(position));
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
                else {
                    View convertView = ((ViewGroup) view).getChildAt(0);

                    ImageView iv = (ImageView) convertView.findViewById(R.id.image_view);
                    TextView tv0 = (TextView) convertView.findViewById(R.id.trikonummerErfassung);
                    TextView tv1 = (TextView) convertView.findViewById(R.id.bezeichnungEins);
                    TextView tv2 = (TextView) convertView.findViewById(R.id.bezeichnungZwei);
                    TextView tv3 = (TextView) convertView.findViewById(R.id.bezeichnungDrei);

                    DBHandler dbHandler = new DBHandler(getActivity().getApplicationContext(), null, null, 1);

                    boolean isGesperrt = false;
                    if(selectableKlasse instanceof Kategorie){
                        if(((Kategorie)selectorList.get(position)).getEigene() == 1){
                            isGesperrt = true;
                            String fehlermeldung = "Sie dürfen den Status dieser Kategorie nicht ändern!";

                            Dialog d = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("Achtung")
                                    .setMessage(fehlermeldung)
                                    .setNegativeButton("OK", null)
                                    .create();
                            d.show();
                        }
                    }

                    if(!isGesperrt) {
                        if (selectorList.get(position).getSelected() == 1) {
                            Log.d("Selected", "Wird deaktiviert");
                            selectorList.get(position).setSelected(0);
                            deaktiviereSchaltflächen(iv, tv0, tv1, tv2, tv3);
                        } else {
                            Log.d("Selected", "Wird aktiviert");
                            selectorList.get(position).setSelected(1);
                            aktiviereSchaltflächen(iv, tv0, tv1, tv2, tv3);
                        }
                    }


                    if (isDatabaseUpdating) {
                        dbHandler.update(selectorList.get(position));
                    }
                    dbHandler.close();

                    updateListView();
                }
            }
        }
    };

    SwipeMenuListView.OnMenuItemClickListener onMenuClickListener = new SwipeMenuListView.OnMenuItemClickListener() {

        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
            //String value = adapter.getItem(position);
            SelectableItem selectable = selectorList.get(position);
            setPos(position);
            DBHandler dbHandler = new DBHandler(activity.getApplicationContext(), null, null, 1);
            final Object object = dbHandler.find(selectable.getId(), selectableKlasse);

            switch (index) {
                case 0:

                    Intent intent = null;

                    if(selectableKlasse instanceof Statistik){
                        Statistik statistik = (Statistik) object;
                        intent = new Intent(activity, ErfasseNeueStatiActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("Update", true);
                        bundle.putParcelable("Statistik", statistik);

                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }
                    else if(selectableKlasse instanceof Spieler) {
                        Spieler spieler = (Spieler) object;
                        intent = new Intent(activity, NewSpielerActivity.class);

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("Update", true);
                        bundle.putParcelable("Spieler", spieler);

                        intent.putExtras(bundle);
                        activity.startActivity(intent);
                    }

                    else if(selectableKlasse instanceof Kategorie) {
                        Kategorie kategorie = (Kategorie) object;

                        if(kategorie.getEigene() == 0) {
                            intent = new Intent(activity, NewKategorieActivity.class);

                            Bundle bundle = new Bundle();
                            bundle.putBoolean("Update", true);
                            bundle.putParcelable("Kategorie", kategorie);

                            intent.putExtras(bundle);
                            activity.startActivity(intent);
                        }
                        else{
                            String fehlermeldung = "Sie dürfen diese Kategorie nicht ändern!";

                            Dialog d = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK)
                                    .setTitle("Achtung")
                                    .setMessage(fehlermeldung)
                                    .setNegativeButton("OK", null)
                                    .create();
                            d.show();
                        }
                    }

                    break;

                case 1:
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                switch (which) {
                                    case DialogInterface.BUTTON_POSITIVE:
                                        //Yes button clicked
                                        DBHandler dbHandler = new DBHandler(activity.getApplicationContext(), null, null, 1);
                                        dbHandler.delete(selectorList.get(getPos()));

                                        updateListView();
                                        break;


                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                    };

                    if(selectableKlasse instanceof Kategorie && ((Kategorie)object).getEigene() == 1){
                        String fehlermeldung = "Sie dürfen diese Kategorie nicht löschen!";

                        Dialog d = new AlertDialog.Builder(activity, AlertDialog.THEME_HOLO_DARK)
                                .setTitle("Achtung")
                                .setMessage(fehlermeldung)
                                .setNegativeButton("OK", null)
                                .create();
                        d.show();
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(selectable.getText1() + " " + selectable.getText2() + " wirklich löschen?").setPositiveButton("JA", dialogClickListener)
                                .setNegativeButton("NEIN", dialogClickListener).show();
                    }
            }
            return false;
        }
    };

    public class CustomArrayAdapter extends ArrayAdapter<T> {
        protected LayoutInflater inflater;
        protected int layout;

        public CustomArrayAdapter(Activity activity, int resourceId, ArrayList<T> objects){
            super(activity, resourceId, objects);
            layout = resourceId;
            inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SelectableItem item = getItem(position);

            if(convertView == null) {
                convertView = inflater.inflate(layout, parent, false);
            }

            if(layout == R.layout.swipemenu_item) {
                ImageView iv = (ImageView) convertView.findViewById(R.id.image_view);
                TextView tv0 = (TextView) convertView.findViewById(R.id.trikonummerErfassung);
                TextView tv1 = (TextView) convertView.findViewById(R.id.bezeichnungEins);
                TextView tv2 = (TextView) convertView.findViewById(R.id.bezeichnungZwei);
                TextView tv3 = (TextView) convertView.findViewById(R.id.bezeichnungDrei);

                if (item instanceof Spieler) {
                    Spieler spieler = (Spieler) item;

                    iv.setImageBitmap(spieler.getFoto());
                    tv0.setText(String.valueOf(spieler.getTrikonummer()));
                    tv1.setText(spieler.getNachname() + ",");
                    tv2.setText(spieler.getVorname());
                    String positionen = "";

                    for (int i = 0; i < spieler.getPosition().size(); i++) {
                        positionen += spieler.getPosition().get(i).getNameKurz();
                        if (i < spieler.getPosition().size() - 1) {
                            positionen += ", ";
                        }
                    }

                    if (positionen != "") {
                        tv3.setText("Pos: " + positionen);
                    } else {
                        tv3.setText("Pos: KEINE");
                    }
                }

                if (item instanceof Kategorie) {
                    Kategorie kategorie = (Kategorie) item;
                    Log.d("FOTO", String.valueOf(kategorie.getFoto()));
                    iv.setImageBitmap(kategorie.getFoto());

                    tv1.setText(kategorie.getName());
                    tv2.setText("");
                    tv3.setText("Art: " + kategorie.getArt());

                } else {
                    iv.setImageBitmap(item.getFoto());

                    if (item.getText1() != "") {
                        tv1.setText(item.getText1());
                    }
                    if (item.getText2() != "") {
                        tv2.setText(item.getText2());
                    }
                }

                if ((selectorList.get(position)).getSelected() == 1) {
                    aktiviereSchaltflächen(iv, tv0, tv1, tv2, tv3);
                } else {
                    deaktiviereSchaltflächen(iv, tv0, tv1, tv2, tv3);
                }
            }
            else if(layout == R.layout.swipemenu_item_stati){
                TextView heim = (TextView) convertView.findViewById(R.id.heim);
                TextView gast = (TextView) convertView.findViewById(R.id.gast);
                TextView datum = (TextView) convertView.findViewById(R.id.datum);
                TextView ergebnis = (TextView) convertView.findViewById(R.id.ergebnis);

                if (item instanceof Statistik) {
                    Statistik statistik = (Statistik) item;

                    if(statistik.getHeim() == 1){
                        heim.setText(mannschaft.getVereinsname());
                        gast.setText(statistik.getGegner());
                    }
                    else {
                        heim.setText(statistik.getGegner());
                        gast.setText(mannschaft.getVereinsname());
                    }

                    datum.setText(statistik.getDatum());

                    String ergebnisText = "";
                    if(statistik.getHeim() == 1){
                        ergebnisText = statistik.getEigeneTore() + " : " + statistik.getGegnerTore();
                    }
                    else {
                        ergebnisText = statistik.getGegnerTore() + " : " + statistik.getEigeneTore();
                    }

                    ergebnis.setText(ergebnisText);


                    if(position%2 == 1){
                        convertView.setBackgroundResource(R.drawable.red_button);
                    }
                    else{
                        convertView.setBackgroundResource(R.drawable.green_button);
                    }
                }
            }

            return convertView;
        }
    }
    public void aktiviereSchaltflächen(ImageView iv, TextView tv0, TextView tv1, TextView tv2, TextView tv3){
        imageNormal(iv);
        textBlackOut(tv0);
        textBlackOut(tv1);
        textBlackOut(tv2);
        textBlackOut(tv3);
    }

    public void deaktiviereSchaltflächen(ImageView iv, TextView tv0, TextView tv1, TextView tv2, TextView tv3){
        imageGreyOut(iv);
        textGreyOut(tv0);
        textGreyOut(tv1);
        textGreyOut(tv2);
        textGreyOut(tv3);
    }

    public void imageGreyOut(ImageView v)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);  //0 means grayscale
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
        v.setAlpha(128);   // 128 = 0.5
    }

    public void imageNormal(ImageView v)
    {
        v.setColorFilter(null);
        v.setAlpha(255);
    }

    public void textGreyOut(TextView tv){
        tv.setTextColor(Color.LTGRAY);
    }

    public void textBlackOut(TextView tv){
        tv.setTextColor(Color.BLACK);
    }

    public ArrayList<T> getListe(T klasse){

        DBHandler dbHandler = new DBHandler(getActivity().getApplicationContext(), null, null, 1);
        ArrayList<T> liste = new ArrayList<>();


        if(isUnactivItemsShowing) {
            if (klasse instanceof Spieler) {
                liste = (ArrayList<T>) dbHandler.getSpielerDerMannschaft(mannschaft);
            }
            else if (klasse instanceof Kategorie) {
                liste = (ArrayList<T>) dbHandler.getKategorienDerMannschaft(mannschaft);
            }
            else if (klasse instanceof Statistik) {
                liste = (ArrayList<T>) dbHandler.getStatistikenDerMannschaft(mannschaft);
            }

        }
        else{
            if (klasse instanceof Spieler) {
                liste = (ArrayList<T>) dbHandler.getAktiveSpielerDerMannschaft(mannschaft);
            }
            else if (klasse instanceof Kategorie) {
                liste = (ArrayList<T>) dbHandler.getAktiveKategorienDerMannschaft(mannschaft);
            }
        }

        if(!isStatusChanging){
            for(int i=0; i<liste.size(); i++){
                liste.get(i).setSelected(1);
            }
        }

        dbHandler.close();

        return liste;

    }

    public ArrayList<T> sortListe(ArrayList<T> liste){

        ArrayList<T> sortListe = new ArrayList<T>();

        if(selectableKlasse instanceof Spieler){

            ArrayList<Spieler> aktiv = new ArrayList<Spieler>();
            ArrayList<Spieler> inaktiv = new ArrayList<Spieler>();

            for (int i=0; i<liste.size(); i++){
                if(liste.get(i).getSelected() == 1){
                    aktiv.add((Spieler)liste.get(i));
                }
                else{
                    inaktiv.add(((Spieler)liste.get(i)));
                }
            }

            Collections.sort(aktiv, new SortTrikonummer());
            Collections.sort(inaktiv, new SortTrikonummer());

            sortListe.addAll((ArrayList<T>)aktiv);
            sortListe.addAll((ArrayList<T>)inaktiv);

        }
        else if(selectableKlasse instanceof Kategorie){
            ArrayList<Kategorie> gesperrte = new ArrayList<Kategorie>();
            ArrayList<Kategorie> aktiv = new ArrayList<Kategorie>();
            ArrayList<Kategorie> inaktiv = new ArrayList<Kategorie>();

            for (int i=0; i<liste.size(); i++){
                if(((Kategorie)liste.get(i)).getEigene() == 1){
                    gesperrte.add((Kategorie)liste.get(i));
                }
                else if(liste.get(i).getSelected() == 1){
                    aktiv.add((Kategorie)liste.get(i));
                }
                else{
                    inaktiv.add(((Kategorie)liste.get(i)));
                }
            }

            Collections.sort(gesperrte, new SortName());
            Collections.sort(aktiv, new SortName());
            Collections.sort(inaktiv, new SortName());

            sortListe.addAll((ArrayList<T>)gesperrte);
            sortListe.addAll((ArrayList<T>)aktiv);
            sortListe.addAll((ArrayList<T>)inaktiv);
        }
        else{
            sortListe = liste;
        }

        return sortListe;
    }

    public ArrayList<? extends SelectableItem> getAllAktivItems(){
        ArrayList<? extends SelectableItem> aktivItems = new ArrayList<>(selectorList);

        int i=0;
        while(i < aktivItems.size()){
            if(aktivItems.get(i).getSelected() == 0){
                aktivItems.remove(i);
            }
            else{
                i++;
            }
        }

        return aktivItems;
    }

    public Activity getActivity() {
        return activity;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }
}
