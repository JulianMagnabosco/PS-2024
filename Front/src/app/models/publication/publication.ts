export interface Publication {
  name: string;
  description: string;
  type: string;
  difficulty: string;
  image: string;
  conditions: string[];
  materials: string[];
  steps: string[];
  canSold: boolean;
  price: number;
  count: number
}
