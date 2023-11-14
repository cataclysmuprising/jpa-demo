-- To create random String with given length
DROP FUNCTION IF EXISTS get_random_string(INTEGER);
CREATE FUNCTION get_random_string(length INTEGER)
RETURNS text AS $$
	DECLARE
	  chars text[] := '{0,1,2,3,4,5,6,7,8,9,A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z}';
	  result text := '';
	  i INTEGER := 0;
	BEGIN
	  IF length < 0 THEN
	    RAISE EXCEPTION 'Given length cannot be less than 0';
	  END IF;
	  for i in 1..length LOOP
	    result := result || chars[1+random()*(array_length(chars, 1)-1)];
	  END LOOP;
	  RETURN result;
	END;
$$ LANGUAGE plpgsql;