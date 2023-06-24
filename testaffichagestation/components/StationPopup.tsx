import { Station, point } from "../types/LinesAttributes";
import style from "./StationPopup.module.scss";

export default function StationPopup({ station }: { station: Station }) {
    const popupPosition: point = {
        x: station.position.x - 100,
        y: station.position.y - 100, // Positionnement 20 pixels au-dessus de la station
    };

    return (
        <foreignObject x={popupPosition.x} y={popupPosition.y} width="500" height="100">
            <div className={style.popup}>
                <span>{station.displayName}</span>
            </div>
        </foreignObject>
    );
}