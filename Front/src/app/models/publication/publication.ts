import {Section} from "./section";

export interface Publication {
  id: number;
  name: string;
  description: string;
  type: string;
  userId: number;
  difficulty: string;
  calification: number;
  myCalification: number;
  sections: Section[];
  canSold: boolean;
  price: number;
  count: number
}
