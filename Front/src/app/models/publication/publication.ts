import {Section} from "./section";

export interface Publication {
  id: number;
  name: string;
  description: string;
  type: string;
  difficulty: string;
  imageUrl: string;
  sections: Section[];
  canSold: boolean;
  price: number;
  count: number
}
