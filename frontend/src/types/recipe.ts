export interface Recipe {
  id: number;
  name: string;
  description: string;
  imageUrl: string;
  time: string;
  category: string;
  equipments: string[];
  ingredients: string[];
  instructions: string[];
  tags: string[];
  views: number;
  likes: number;
  dislikes: number;
  comments: number;
}
