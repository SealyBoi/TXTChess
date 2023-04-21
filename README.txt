 ===================================================================
  ####  #    # ######  ####   ####          #   ##   #    #   ##   
 #    # #    # #      #      #              #  #  #  #    #  #  #  
 #      ###### #####   ####   ####          # #    # #    # #    # 
 #      #    # #           #      #         # ###### #    # ###### 
 #    # #    # #      #    # #    # ## #    # #    #  #  #  #    # 
  ####  #    # ######  ####   ####  ##  ####  #    #   ##   #    # 
===================================================================

/ * This is the finished product for a two-player game of chess with no chess AI * /

This is my rendition of chess, but in a txt format.

You can either play yourself in singleplayer, or you can play with a friend via multiplayer.

-To play singleplayer, or pass-and-play, simply select the 'Play' option.

-To play multiplayer, have Player 1 select 'Host' and Player 2 select 'Connect'.
 Player 1 will give Player 2 the IP to connect to (the number following the /).
 Afterwards both players should automatically connect and gameplay should begin.

To compile, simply type 'make' in the txtchess directory.
To launch application, simply type 'java Main/Main'.

To play simply type the format of "{piece}{currCol}{currRow}{col}{row}" where:
    -'piece' can be p, r, n, b, q, k (pawn, rook, knight, bishop, queen, king respectively)
    -'currCol'/'currRow' are that pieces current location
    -'col'/'row' are where you are trying to move the piece.

Ex: pe2e4 (opening pawn move from position e2 to e4)
    ra1a8 (rook move from the bottom of the a file to the top)
    nb1c3 (knight move from starting position to center defense)