package ulaladoongdoong.sunwaykasanyamuk.printing;
/**
 * This is Custom Print Adapter
 * <p>
 * the process is that everything
 * 1. will be converted to a pdf file
 * 2. the pdf file will be output to a specific folder in this case
 * it is 'parcelFileDescriptor' folder. [it is defined in the onWrite function]
 * 3. the print manager will then use the .print function to print that shit
 * <p>
 * Can be used to print anything!!
 * Be it Canvas, View. Everything
 */


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.print.PageRange;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintDocumentInfo;
import android.print.pdf.PrintedPdfDocument;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Heartless on 16/12/2017.
 */


public class CustomPrintAdapter extends PrintDocumentAdapter {

    Context context;
    private int pageHeight;
    private int pageWidth;
    public PdfDocument myPdfDocument;
    public int totalpages = 4;
    View view;


    //debug tag
    String TAG = "CustomPrintAdapter";

    public CustomPrintAdapter(Context context, View mLV) {
        this.context = context;
        this.view = mLV;
    }

    @Override
    public void onLayout(PrintAttributes oldAttributes, PrintAttributes newAttributes, CancellationSignal cancellationSignal, LayoutResultCallback callback, Bundle bundle) {
        myPdfDocument = new PrintedPdfDocument(context, newAttributes);

        pageHeight = newAttributes.getMediaSize().getHeightMils() / 1000 * 72;
        pageWidth = newAttributes.getMediaSize().getWidthMils() / 1000 * 72;


        Log.d(TAG, "onLayout is called");

        if (cancellationSignal.isCanceled()) {
            Log.d(TAG, "Cancellation signal here");
            callback.onLayoutCancelled();
            return;
        }

        if (totalpages > 0) {
            PrintDocumentInfo.Builder builder = new PrintDocumentInfo
                    .Builder("print_output.pdf")
                    .setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                    .setPageCount(totalpages);

            PrintDocumentInfo info = builder.build();
            callback.onLayoutFinished(info, true);
        } else {
            callback.onLayoutFailed("Page count is zero.");
        }
    }


    @Override
    public void onWrite(PageRange[] pageRanges, ParcelFileDescriptor parcelFileDescriptor, CancellationSignal cancellationSignal, WriteResultCallback callback) {
        for (int i = 0; i < totalpages; i++) {
            if (pageInRange(pageRanges, i)) {
                PdfDocument.PageInfo newPage = new PdfDocument.PageInfo.Builder(pageWidth,
                        pageHeight, i).create();

                PdfDocument.Page page =
                        myPdfDocument.startPage(newPage);

                if (cancellationSignal.isCanceled()) {
                    callback.onWriteCancelled();
                    myPdfDocument.close();
                    myPdfDocument = null;
                    return;
                }
                drawPage(page, i);
                myPdfDocument.finishPage(page);
            }
        }

        try {
            myPdfDocument.writeTo(new FileOutputStream(parcelFileDescriptor.getFileDescriptor()));
        } catch (IOException e) {
            callback.onWriteFailed(e.toString());
            return;
        } finally {
            myPdfDocument.close();
            myPdfDocument = null;
        }

        callback.onWriteFinished(pageRanges);
    }

    private boolean pageInRange(PageRange[] pageRanges, int page) {
        for (int i = 0; i < pageRanges.length; i++) {
            if ((page >= pageRanges[i].getStart()) &&
                    (page <= pageRanges[i].getEnd()))
                return true;
        }
        return false;
    }


    /**
     * this is the function that draws shits on the printed page.
     *
     * @param page the page file on which something is drawn on
     * @param pagenumber indicates the page that is currently being printed
     */
    private void drawPage(PdfDocument.Page page, int pagenumber) {

        //Measure the view at the exact dimensions (otherwise the text won't center correctly)
        int measureWidth = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getWidth(), View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(page.getCanvas().getHeight(), View.MeasureSpec.EXACTLY);


        view.measure(measureWidth, measuredHeight);
        view.layout(0, 0, page.getCanvas().getWidth(), page.getCanvas().getHeight());

        Canvas canvas = page.getCanvas();

        float scalingFactor = 0.001f;
        view.setScaleX(scalingFactor);
        view.setScaleY(scalingFactor);
        //Translate the Canvas into position and draw it
        canvas.save();
        view.draw(canvas);
        canvas.restore();

//        Canvas canvas = page.getCanvas();
//
//        pagenumber++; // Make sure page numbers start at 1
//
//        int titleBaseLine = 72;
//        int leftMargin = 54;
//
//        Paint paint = new Paint();
//        paint.setColor(Color.BLACK);
//        paint.setTextSize(40);
//        canvas.drawText(
//                "Test Print Document Page " + pagenumber,
//                leftMargin,
//                titleBaseLine,
//                paint);
//
//        paint.setTextSize(14);
//        canvas.drawText("This is some test content to verify that custom document printing works", leftMargin, titleBaseLine + 35, paint);
//
//        if (pagenumber % 2 == 0)
//            paint.setColor(Color.RED);
//        else
//            paint.setColor(Color.GREEN);
//
//        PdfDocument.PageInfo pageInfo = page.getInfo();
//
//
//        canvas.drawCircle(pageInfo.getPageWidth() / 2,pageInfo.getPageHeight() / 2,150,paint);
    }
}