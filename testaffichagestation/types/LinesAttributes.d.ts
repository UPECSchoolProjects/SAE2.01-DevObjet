

/* export type Station = {
    id: string,
    displayName: string,
    correspondances: string[],
} */

export type Station = {
    id: string,
    idfmId: string,
    idName: string,
    displayName: string,
    line: string,
    position: point,
    displayType: "normal" | "correspTerminus" | "correspondanceSimple" | "none"
}

export type Troncons = {
    id: string,
    d: string,
    beginStation: string,
    endStation: string,
    line?: string,
}

export type GraphicCorrespondance = {
    id: string,
    displayName: string
    stations: {id: number, line: string}[],
    d: string,
    strokeColor: string,
    strokeWidth: string,
}

export type linedata = {
    strokeColor: string,
    strokeWidth: string
}

export type pathDataType = {
    linedata: { strokeColor: string, strokeWidth: string },
    troncons: (pathAttr & { id: string, beginStation: string, endStation: string })[],
    stations: { [key: string]: Station }
};


export type StopPointData = {
    point: point,
    station: Station
};


export interface pathAttr { id: string, d: string }

export type point = { x: number, y: number };