package hk.ust.aed.menu.adapters;

/**
 * Created by pratap.kesaboyina on 24-12-2014.
 */

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import hk.ust.aed.menu.InfoFragment;
import hk.ust.aed.menu.R;
import hk.ust.aed.menu.models.SectionDataModel;

public class RecyclerViewDataAdapter extends RecyclerView.Adapter<RecyclerViewDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private InfoFragment parent;

    public RecyclerViewDataAdapter(InfoFragment parent, ArrayList<SectionDataModel> dataList) {
        this.dataList = dataList;
        this.mContext = parent.getContext();
        this.parent = parent;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        TextView instructionsTxt = (TextView) v.findViewById(R.id.instructionsTxt);
        instructionsTxt.setText(getInstructions());
        ItemRowHolder mh = new ItemRowHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {

        final String sectionName = dataList.get(i).getHeaderTitle();

        ArrayList singleSectionItems = dataList.get(i).getAllItemsInSection();

        itemRowHolder.itemTitle.setText(sectionName);

        SectionListDataAdapter itemListDataAdapter = new SectionListDataAdapter(mContext, singleSectionItems);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);

        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);


       /*  itemRowHolder.recycler_view_list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        //Allow ScrollView to intercept touch events once again.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle RecyclerView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });*/

        itemRowHolder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Starting game!" , Toast.LENGTH_SHORT).show();
                parent.startGame();
            }
        });

       /* Glide.with(mContext)
                .load(feedItem.getImageURL())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .error(R.drawable.bg)
                .into(feedListRowHolder.thumbView);*/
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {

        protected TextView itemTitle;

        protected RecyclerView recycler_view_list;

        protected Button btnMore;



        public ItemRowHolder(View view) {
            super(view);

            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.btnMore= (Button) view.findViewById(R.id.btnBegin);
        }

    }

    public String getInstructions(){
        switch(parent.fragmentState){
            case SWM_INFO:
                return "The objective of each trial in the game is to unhide the yellow boxes under each red box. However, exactly one yellow box will be visible at a certain time, in a random order. By trial and error, the user must find all of the yellow boxes, in as little time as possible.";
            case SRM_INFO:
                return "The user must memorize the position of the red boxes presented at the start of each trial, when they are vibrating - the progress indicator will also be yellow at this time. These boxes will be presented in successive order in the following screens, along with other boxes at new locations. The user must identify and tap on the box at a familiar location as quickly as possible, when the progress bar is green.";
            case MTT_INFO:
                return "To indicate the start of each trial, the arrow and text will flash in white. If the text says \"direction\", then observe which way the arrow is pointing, and click the appropriate button. If the text says \"location\", you must instead take into account whether the arrow is at the left or right of the screen for your answer.";
            case UNITY_GAME_INFO:
                return "This game consists of two parts: sign task and coin collection task.\n" +
                        "For the sign task, the user must tap the screen when the green sign appears. He/she must NOT tap the screen when the red sign appears.\n" +
                        "For the coin collection task, the user must tilt the phone to move the teddy bear so that it can collect the coins that appear on the road.\n" +
                        "During calibration, these two tasks are played separately.\n" +
                        "Once calibration is done, the user can access the multitask test where both tasks are running simultaneously.";
        }
        return "";
    }
}