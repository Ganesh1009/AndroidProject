package de.innocow.innocow_v001.activities;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.elconfidencial.bubbleshowcase.BubbleShowCaseBuilder;

import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import de.innocow.innocow_v001.R;
import de.innocow.innocow_v001.pojo.cowsearch.CowTagResponse;
import de.innocow.innocow_v001.pojo.cowsearch.UnEquippedTagList;
import de.innocow.innocow_v001.utilities.retrofit.RestClient;
import de.innocow.innocow_v001.utilities.retrofit.RetrofitBuilder;
import de.innocow.innocow_v001.utilities.string_constants.StringConstants;
import de.innocow.innocow_v001.utilities.views.recycler_views.RecyclerViewTagWaehlen;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TagWaehlen extends AppCompatActivity implements OnTagEquipClickListener {

    private String cowId;
    private List<String> unequippedTags;
    private NfcAdapter nfcAdapter;
    private RecyclerView recyclerView;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_waehlen);

        TextView cowNameTextView = findViewById(R.id.tvCowName);
        TextView cowIdTextView = findViewById(R.id.tvCowId);
        TextView stallTextView = findViewById(R.id.stall_text_view);
        recyclerView = findViewById(R.id.rvTagWaehlen);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        if (intent != null && intent.getExtras() != null) {
            cowNameTextView.setText(intent.getStringExtra(StringConstants.COW_NAME));
            cowIdTextView.setText(intent.getStringExtra(StringConstants.COW_ID));
            cowId = cowIdTextView.getText().toString();
            String shedNumber = intent.getExtras().getString("shedNumber");
            stallTextView.setText(shedNumber);


            unequippedTags = new ArrayList<>();

            @SuppressWarnings("unchecked")
            List<UnEquippedTagList> unequippedTagList = (List<UnEquippedTagList>)
                    intent.getSerializableExtra(StringConstants.UNEQUIPPED_TAG_LIST);

            for (UnEquippedTagList tagList : unequippedTagList) {
                unequippedTags.add(tagList.getUnEquippedTag());
            }

            recyclerView.setAdapter(new RecyclerViewTagWaehlen(TagWaehlen.this, unequippedTags));

            SearchView tagSearchView = findViewById(R.id.searchView);
            tagSearchView.clearFocus();
            tagSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    List<String> searchedTags = new ArrayList<>();
                    for (String unequippedTag : unequippedTags) {
                        if (StringUtils.containsIgnoreCase(unequippedTag, newText)) {
                            searchedTags.add(unequippedTag);
                        }
                    }
                    recyclerView.setAdapter(new RecyclerViewTagWaehlen(TagWaehlen.this, searchedTags));
                    return false;
                }
            });

            nfcOverlay(tagSearchView);

            this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);

            pendingIntent = PendingIntent.getActivity(this, 0,
                    new Intent(this, getClass())
                            .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (nfcAdapter != null) {

            if (!nfcAdapter.isEnabled())
                enableNfc();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (nfcAdapter != null)
            nfcAdapter.disableForegroundDispatch(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {

        setIntent(intent);
        resolveIntent(intent);
    }

    @Override
    public void tagEquipClick(final String tagName) {

        RestClient client = RetrofitBuilder.getRestClient();
        SharedPreferences preferences = getApplicationContext()
                .getSharedPreferences(StringConstants.APP_PREFERENCE, MODE_PRIVATE);
        String token = preferences.getString(StringConstants.TOKEN, null);
        long farmId = preferences.getLong(StringConstants.FARM_ID, 0);
        Call<CowTagResponse> call = client.assignTagToCow(token, tagName, farmId, cowId);
        call.enqueue(new Callback<CowTagResponse>() {
            @Override
            public void onResponse(@NonNull Call<CowTagResponse> call
                    , @NonNull Response<CowTagResponse> response) {
                Log.d("tagequipsuccess", String.valueOf(response.body()));

                if (response.body() != null && StringUtils.isNotBlank(response.body().getValue())) {
                    Log.d("tagequipsuccess", response.body().toString());

                    showSuccessAlertDialog(tagName);
                    final Timer t = new Timer();
                    t.schedule(new TimerTask() {
                        public void run() {
                            t.cancel(); // also just top the timer thread, otherwise, you may receive a crash report
                            setResult(1);
                            finish();
                        }
                    }, 5000);
                }

            }

            @Override
            public void onFailure(@NonNull Call<CowTagResponse> call, @NonNull Throwable t) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void nfcOverlay(View fullScreenOverlay) {
        BubbleShowCaseBuilder bubbleShowCaseBuilder = new BubbleShowCaseBuilder(this);
        bubbleShowCaseBuilder
                .title("NFC")
                .description("Halte das Handy über den Tag, welches an dieser Kuh angebracht werden soll.")
                .descriptionTextSize(18)
                .backgroundColorResourceId(R.color.colorPrimary)
                .imageResourceId(R.drawable.ic_scantag)
                .showOnce("BUBBLE_SHOW_CASE_TAG_ID")
                .targetView(fullScreenOverlay)
                .show();

    }

    private void showSuccessAlertDialog(String tagName) {
        Dialog dialog = new Dialog(TagWaehlen.this);
        dialog.setContentView(R.layout.success_dialog);
        TextView alertMessage = dialog.findViewById(R.id.alertMessage);
        alertMessage.setText(String.format(StringConstants.MSG_SUCCESSFUL_TAG_EQUIP, tagName));
        dialog.show();
    }

    private void enableNfc() {

        Toast.makeText(this, StringConstants.MSG_ENABLE_NFC, Toast.LENGTH_SHORT).show();
        Intent enableNfcIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(enableNfcIntent);
    }

    private void resolveIntent(Intent nfcIntent) {

        String intentAction = nfcIntent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intentAction) ||
                NfcAdapter.ACTION_TECH_DISCOVERED.equals(intentAction) ||
                NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intentAction)
        ) {
            Parcelable[] nfcRawMessages = nfcIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            List<NdefMessage> ndefMessageList = null;
            if (nfcRawMessages != null) {

                ndefMessageList = new ArrayList<>();

                for (Parcelable rawMessage : nfcRawMessages) {

                    ndefMessageList.add((NdefMessage) rawMessage);
                }
            }

            if (ndefMessageList != null) {

                NdefRecord record = ndefMessageList.get(0).getRecords()[1];
                byte[] payload = record.getPayload();
                String payloadString = new String(payload, StandardCharsets.UTF_8);
                String tagIdInString = payloadString.substring(payloadString.lastIndexOf(":") + 1);
                String deviceNumber = String.valueOf(Long.parseLong(tagIdInString, 16));
                int tagId = Integer.valueOf(deviceNumber.substring(deviceNumber.length() - 4));
                tagEquipClick("TAG-" + tagId);
            }
        }
    }
}
