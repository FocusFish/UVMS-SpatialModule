package eu.europa.ec.fisheries.uvms.spatial.service.bean.dto.usm;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by padhyad on 11/25/2015.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StyleSettingsDto {

    @JsonProperty("positions")
    private PositionsDto positions;

    @JsonProperty("segments")
    private SegmentsDto segments;

    public StyleSettingsDto() {}

    public StyleSettingsDto(PositionsDto positions, SegmentsDto segments) {
        this.positions = positions;
        this.segments = segments;
    }

    @JsonProperty("positions")
    public PositionsDto getPositions() {
        return positions;
    }

    @JsonProperty("positions")
    public void setPositions(PositionsDto positionsDto) {
        this.positions = positionsDto;
    }

    @JsonProperty("segments")
    public SegmentsDto getSegments() {
        return segments;
    }

    @JsonProperty("segments")
    public void setSegments(SegmentsDto segmentsDto) {
        this.segments = segmentsDto;
    }

    @Override
    public String toString() {
        return "ClassPojo [positions = " + positions + ", segments = " + segments + "]";
    }
}
