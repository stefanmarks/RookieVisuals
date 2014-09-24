package modifier;

import analyser.SpectrumAnalyser;
import timing.TimeBase;

/**
 * Modifier that changes a parameter based on the intensity of a frequency in an audio signal.
 * 
 * @author  Stefan Marks
 */
public class AudioSignal_FrequencyIntensity extends AbstractSingleModifier
{
    public AudioSignal_FrequencyIntensity(String paramName, SpectrumAnalyser analyser, int spectrumIdx, float min, float max)
    {
        super(paramName);
        this.analyser    = analyser;
        this.spectrumIdx = spectrumIdx;
        this.min         = min;
        this.max         = max;
        runningMax = 0.1f;
    }
    
    
    @Override
    public void apply(TimeBase timeBase)
    {
        final float intensity = analyser.getSpectrumInfo(0).intensity[spectrumIdx];
        runningMax = Math.max(runningMax, intensity);
        visualParam.set(min + (max - min) * (intensity / runningMax));
    }

    
    private final SpectrumAnalyser analyser;
    private final float            min, max;
    private final int              spectrumIdx;
    private       float            runningMax;
}
