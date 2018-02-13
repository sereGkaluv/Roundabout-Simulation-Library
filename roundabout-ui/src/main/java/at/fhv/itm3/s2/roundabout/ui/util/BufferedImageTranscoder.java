package at.fhv.itm3.s2.roundabout.ui.util;


import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.awt.image.BufferedImage;

public class BufferedImageTranscoder extends ImageTranscoder {

    private BufferedImage img = null;

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage createImage(int width, int height) {
        return new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void writeImage(BufferedImage img, TranscoderOutput to)
    throws TranscoderException {
        this.img = img;
    }

    public BufferedImage getBufferedImage() {
        return img;
    }
}