interface StationRelation {
    id: string;
    ligne: string;
  }
  
  interface RelationFromBackend {
    st1: StationRelation;
    st2: StationRelation;
    temps: number;
  }