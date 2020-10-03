package com.nwp.game.source;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.nwp.game.Game;
import com.nwp.game.objects.Ball;

public class Gutter {
    private int y_level;
    private Vector2 velocity;
    private int num_filled_balls;
    private int num_balls_in_line;
    private float device_width;
    private int direction;
    private float length_btw_balls=10;
    private Array<Ball> balls;
    private Array<Texture> textures;
    private boolean is_move_balls = true;

    //random generation balls vars
    private int last_double_index = -1;
    private int texture_index = 0;
    private Array<Texture> three_ball_ahead;

    public Gutter(int y_level,
                  float x_velocity,
                  int num_balls_in_line,
                  int num_filled_balls,
                  float devise_width,
                  Array<Texture> possible_level_textures){
        this.y_level= y_level;
        this.direction = x_velocity>0?1:-1;
        this.velocity = new Vector2(x_velocity,0);
        this.num_balls_in_line = num_balls_in_line;
        this.num_filled_balls = num_filled_balls;
        this.device_width = devise_width;
        this.textures = possible_level_textures;
        this.set_three_next_texture();
        this.balls = new Array<>();
        spawn_ball();
    }

    public void spawn_ball(){
        Texture tmp_texture = three_ball_ahead.get(texture_index);
        texture_index++;
        balls.add(new Ball(tmp_texture,velocity,y_level));
        if (texture_index==3){
            this.set_three_next_texture();
            texture_index=0;
        }
    }

    public void render_gutter(SpriteBatch batch, Ball shoot_ball){
        for(Ball ball: balls) {
            if (shoot_ball!=null) {
                if (shoot_ball.position.x != 0 && is_move_balls && ball.ballSprite.getBoundingRectangle().overlaps(shoot_ball.ballSprite.getBoundingRectangle())) {
                    handle_collision(shoot_ball,ball);
                    shoot_ball.position.set(0,0);
//					new_ball = balls.get(ball_pos-1);
//					if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
//						new_ball = balls.get(ball_pos-2);
//						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
//							pos_collapse = ball_pos-3;
//							cord_collapse = new Vector2(balls.get(ball_pos).position);
//							balls.removeRange(ball_pos-2,ball_pos);
//						}
//						else {
//							new_ball = balls.get(ball_pos+1);
//							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
//								pos_collapse = ball_pos-2;
//								cord_collapse = new Vector2(balls.get(ball_pos+1).position);
//								balls.removeRange(ball_pos-1,ball_pos+1);
//							}
//						}
//					}
//					else {
//						new_ball = balls.get(ball_pos+1);
//						if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
//							new_ball = balls.get(ball_pos+2);
//							if (new_ball.ballSprite.getTexture().toString().equals(shoot_tex)){
//								pos_collapse = ball_pos-1;
//								cord_collapse = new Vector2(balls.get(ball_pos+2).position);
//								balls.removeRange(ball_pos,ball_pos+2);
//							}
//						}
//					}
//					if (pos_collapse!=-1){
//						pos_collapse++;
//						for(int i = pos_collapse; i>=0;i--){
//							Vector2 speed = new Vector2(balls.get(i).velocity);
//							balls.get(i).velocity.set(speed.rotate(180).scl(1.5f,1.5f));
//						}
//					}
//					else
//						is_move_balls = true;
                }
            }
            if(direction==1 && ball.position.x>=device_width+ball.ballSprite.getWidth() ||
                    direction==0 && ball.position.x+ball.ballSprite.getWidth()<=0){
                balls.removeValue(ball,false);
            }
            if(direction==1 && ball.position.x==length_btw_balls ||
                    direction==0 && ball.position.x+ball.ballSprite.getWidth()+length_btw_balls==device_width){
                spawn_ball();
            }
            ball.update();
            ball.ballSprite.draw(batch);
        }
    }

    private void set_three_next_texture(){
        int index_double_texture = Game.get_rand_in_range(0,textures.size-1);
        int index_another_texture=Game.get_rand_in_range(0,textures.size-1);
        if (this.last_double_index!=-1) {
            if (index_double_texture==last_double_index) {
                if (index_double_texture != 0) {
                    if (index_double_texture == textures.size - 1)
                        index_double_texture--;
                    else
                        index_double_texture++;
                } else
                    index_double_texture++;
            }
        }
        if (index_another_texture==index_double_texture){
            if (index_another_texture!=0){
                if (index_another_texture==textures.size-1)
                    index_another_texture--;
                else
                    index_another_texture++;
            }
            else
                index_another_texture++;
        }
        three_ball_ahead = new Array<Texture>();
        if (Game.get_rand_in_range(0,1)==0)
            three_ball_ahead.add(textures.get(index_another_texture),textures.get(index_double_texture),textures.get(index_double_texture));
        else
            three_ball_ahead.add(textures.get(index_double_texture),textures.get(index_double_texture),textures.get(index_another_texture));
        this.last_double_index = index_double_texture;
    }

    private void handle_collision(Ball shoot_ball, Ball ball){
        is_move_balls = false;
        Vector2 pos_insert;
        int ball_pos;
        if (direction==1 && shoot_ball.position.x<=ball.position.x ||
                direction==0 && shoot_ball.position.x>=ball.position.x){
            ball_pos = balls.indexOf(ball,false)+1;
            pos_insert = new Vector2(balls.get(ball_pos).position);
        }
        else {
            pos_insert = new Vector2(ball.position);
            ball_pos = balls.indexOf(ball,false);
        }

        Ball new_ball = new Ball(shoot_ball.ballSprite.getTexture(),velocity,y_level);
        new_ball.position.set(pos_insert.x, pos_insert.y);
        balls.insert(ball_pos,new_ball);
        //String shoot_tex = ShootBall.ballSprite.getTexture().toString();
        for(int i = ball_pos; i>=0;i--){
            Ball current_ball = balls.get(i);
            if (i != 0){
                Ball next_ball = balls.get(i-1);
                while (current_ball.position.x != next_ball.position.x)
                    current_ball.position.add(current_ball.velocity);
            }
            else {
                Vector2 tmp_position = new Vector2(current_ball.position);
                while (current_ball.position.x != tmp_position.x+current_ball.ballSprite.getWidth()+length_btw_balls)
                    current_ball.position.add(current_ball.velocity);
            }
            current_ball.ballSprite.setPosition(current_ball.position.x, current_ball.position.y);
        }
        is_move_balls = true;
    }
};
